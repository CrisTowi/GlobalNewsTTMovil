package com.example.cris.globalnews;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import android.content.Intent;

import android.util.Log;

public class ListaNoticiasActivity extends Activity {
    ArrayList resultado = new ArrayList();
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_noticias);

        ArrayList image_details = getListData();
    }

    private ArrayList getListData() {

        new AsyncTaskParseJson().execute();

        return resultado;
    }

    // you can make this class as another java file so it will be separated from your main activity.
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "MainActivity.java";
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

            final ListView lv1 = (ListView) findViewById(R.id.custom_list);
            lv1.setAdapter(new CustomListAdapter(ctx, resultado));
            lv1.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Object o = lv1.getItemAtPosition(position);
                    Noticia newsData = (Noticia) o;

                    Toast.makeText(ListaNoticiasActivity.this, "Selected :" + " " + newsData + " " + ids_notas.get(position),
                            Toast.LENGTH_LONG).show();

                    Intent i = new Intent(ctx, NotaActivity.class);

                    i.putExtra("id_nota", ids_notas.get(position).toString());
                    startActivity(i);
                }

            });

        }
    }
}
