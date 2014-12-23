package com.example.cris.globalnews;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class NuevaNoticiaActivity extends ActionBarActivity {

    Context ctx = this;
    String url = "https://globalnewstt.herokuapp.com/nuevo/post/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_noticia);
    }

    public void nuevaNota(View view) {
        new AsyncTaskParseJson().execute();
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... arg0) {
            Looper.prepare();
            TextView TVTitulo = (TextView)findViewById(R.id.ETtitulo);
            TextView TVDescripcion = (TextView)findViewById(R.id.ETDescripcion);
            InputStream is = null;
            String json = "";
            JSONObject jsonO = null;
            int id_nota = 0;

            String titulo = TVTitulo.getText().toString();
            String descripcion = TVDescripcion.getText().toString();

            ServicioLocalizacion servicio = new ServicioLocalizacion(ctx);

            double latitude = servicio.getLatitud();
            double longitude = servicio.getLongitud();

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            try{
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("usuario", "1"));
                nameValuePairs.add(new BasicNameValuePair("subseccion", "1"));
                nameValuePairs.add(new BasicNameValuePair("titulo", titulo));
                nameValuePairs.add(new BasicNameValuePair("descripcion", descripcion));
                nameValuePairs.add(new BasicNameValuePair("latitud", Double.toString(latitude)));
                nameValuePairs.add(new BasicNameValuePair("longitud", Double.toString(longitude)));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                // Execute HTTP Post Request
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity httpEntity = response.getEntity();
                is = httpEntity.getContent();

            } catch (ClientProtocolException e) {
                Log.e("Exception", e.getMessage().toString());
            } catch (IOException e) {
                Log.e("Exception", e.getMessage().toString());
            }
            try {

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "n");
                }
                is.close();
                json = sb.toString();

            } catch (Exception e) {
                Log.e("NuevaNoticiaActivity.java", "Error converting result " + e.toString());
            }



            // try parse the string to a JSON object
            try {
                jsonO = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("NuevaNoticiaActivity.java", "Error parsing data " + e.toString());
            }

            try {
                id_nota = jsonO.getInt("id_nota");
            } catch (JSONException e) {
                Log.e("NuevaNoticiaActivity.java", "Error parsing data " + e.toString());
            }

            Log.e("NuevaNoticiaActivity.java", Integer.toString(id_nota));

            Intent i = new Intent(ctx, NotaActivity.class);

            i.putExtra("id_nota", Integer.toString(id_nota));
            startActivity(i);

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nueva_noticia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
