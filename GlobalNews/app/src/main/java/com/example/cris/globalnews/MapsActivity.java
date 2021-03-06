package com.example.cris.globalnews;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;

import android.location.Location;
import android.location.LocationManager;
import android.content.Context;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener {
    LocationManager locationManager;
    String provider;
    Location location;
    ArrayList resultado = new ArrayList();
    Context ctx = this;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Map<String, Noticia> markersMap = new HashMap<String, Noticia>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        new AsyncTaskParseJson().execute();

    }

    private void setUpMapIfNeeded(ArrayList resultado) {

        ServicioLocalizacion servicio = new ServicioLocalizacion(ctx);

        double lat = servicio.getLatitud();
        double lng = servicio.getLongitud();

        CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(lat, lng));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            mMap.setMyLocationEnabled(true);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);


            if (mMap != null) {
                setUpMap(resultado);
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap(ArrayList resultado) {

        for(int i = 0; i < resultado.size(); i++){
            Noticia n = (Noticia)resultado.get(i);
            mMap.setOnMarkerClickListener(this);

            MarkerOptions marker = new MarkerOptions().position(new LatLng(n.getLatitud(), n.getLongitud())).title(n.getTitulo());

            Marker m = mMap.addMarker(marker);
            markersMap.put(m.getId(), n);
        }

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        Noticia noticia = markersMap.get(marker.getId());
        Intent i = new Intent(ctx, NotaActivity.class);

        i.putExtra("id_nota", Integer.toString(noticia.getId()));
        startActivity(i);

        return false;
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "MapsActivity.java";
        ArrayList results = new ArrayList();
        String url_media = "https://globalnewstt.herokuapp.com/media/";
        ArrayList ids_notas = new ArrayList();

        // set your json string url here
        String url = "https://globalnewstt.herokuapp.com/api/notas/?format=json";

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... arg0) {
            // instantiate our json parser
            JSONParser jParser = new JSONParser();

            // get json string from url
            JSONArray json = jParser.getJSONFromUrl(url);
            try {
                for(int i = 0; i < json.length(); i++){

                    JSONObject jsonO = json.getJSONObject(i);

                    Noticia newsData = new Noticia();
                    newsData.setTitulo(jsonO.getString("titulo"));
                    newsData.setUsuario(jsonO.getString("usuario"));
                    newsData.setFecha(jsonO.getString("fecha"));
                    newsData.setUrl(url_media + jsonO.getString("imagen"));

                    newsData.setLatitud(jsonO.getDouble("latitud"));
                    newsData.setLongitud(jsonO.getDouble("longitud"));

                    newsData.setId(jsonO.getInt("id"));

                    ids_notas.add(jsonO.getInt("id"));
                    results.add(newsData);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            resultado = results;

            setUpMapIfNeeded(resultado);

        }
    }
}
