package com.example.cris.globalnews;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;



import org.json.JSONException;
import org.json.JSONObject;


public class NotaActivity extends Activity {

    String id_nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);
        Bundle bundle = getIntent().getExtras();

        id_nota = bundle.getString("id_nota");
        new AsyncTaskParseJson().execute();

    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "MainActivity.java";
        int id;
        String usuario = "";
        String titulo = "";
        String descripcion = "";
        String imagen = "";
        String fecha = "";
        int likes;

        // set your json string url here
        String url = "https://globalnewstt.herokuapp.com/api/notas/" + id_nota + "/?format=json";

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... arg0) {
            // instantiate our json parser
            JSONParser jParser = new JSONParser();

            // get json string from url
            JSONObject json = jParser.getJSONObjectFromUrl(url);

            try {

                id = json.getInt("id");
                usuario = json.getString("usuario");
                titulo = json.getString("titulo");
                descripcion = json.getString("descripcion");
                imagen = json.getString("imagen");
                fecha = json.getString("fecha");
                likes = json.getInt("likes");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {

            TextView TVId = (TextView) findViewById(R.id.id_nota);
            TVId.setText(Integer.toString(id));
            TextView TVUsuario = (TextView) findViewById(R.id.usuario);
            TVUsuario.setText(usuario);
            TextView TVTitulo = (TextView) findViewById(R.id.titulo);
            TVTitulo.setText(titulo);
            TextView TVDescripcion = (TextView) findViewById(R.id.descripcion);
            TVDescripcion.setText(descripcion);
            TextView TVImagen = (TextView) findViewById(R.id.imagen);
            TVImagen.setText(imagen);
            TextView TVFecha = (TextView) findViewById(R.id.fecha);
            TVFecha.setText(fecha);
            TextView TVLikes = (TextView) findViewById(R.id.likes);
            TVLikes.setText(Integer.toString(likes));
        }
    }

}
