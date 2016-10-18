package com.example.android.marvelapitest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SpiderManActivity extends AppCompatActivity {

    //Constantes, son llaves obtenidas al registrarme en la web de Marvel
    private final String PUBLIC_KEY = "6003c11fab03815a4f07eae64ec8d4ff";
    private final String PRIVATE_KEY = "7293f71f9dff1cabe62769b332c587b686e2c1e7";

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private final String ENLACE_BASE ="http://gateway.marvel.com/v1/public/characters?name=Spider-Man&";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comics);

        TsunamiAsyncTask task = new TsunamiAsyncTask();
        task.execute();
    }


    private class TsunamiAsyncTask extends AsyncTask<URL, Void, ArrayList<Comic>> {

        @Override
        protected ArrayList<Comic> doInBackground(URL... urls) {
            //Primero se debe hacer una operacion con las llaves de marvel para obtener la URL
            // Create URL object
            URL url = createUrl(enlaceMarvel(ENLACE_BASE));

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // TODO Handle the IOException
            }

            ArrayList<Comic> comics = new ArrayList<>();

            if(TextUtils.isEmpty(jsonResponse)){
                return null;
            }

            try {

                //Se debe modificar segun la ruta para spiderman
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);
                JSONObject data = baseJsonResponse.getJSONObject("data");
                JSONArray results = data.getJSONArray("results");
                JSONObject resultsobj = results.getJSONObject(0);
                String heroe = resultsobj.getString("name");
                JSONObject comicsobj = resultsobj.getJSONObject("comics");
                JSONArray itemsarray = comicsobj.getJSONArray("items");
                //Log.v(LOG_TAG,"La ciudad es "+ciudad);
                // If there are results in the features array
                if (itemsarray.length() > 0) {
                    for(int i=0;i<itemsarray.length();i++){
                        JSONObject itemobj = itemsarray.getJSONObject(i);
                        String titulo = itemobj.getString("name");
                        comics.add(new Comic(titulo, heroe));
                    }

                    /*
                    // Create a new {@link Event} object
                    return new UsageEvents.Event(title, time, tsunamiAlert);
                    */
                    return comics;
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return comics;
        }

        /**
         * Update the screen with the given earthquake (which was the result of the
         * {@link TsunamiAsyncTask}).
         */
        @Override
        protected void onPostExecute(ArrayList<Comic> comics) {
            if (comics == null) {
                return;
            }

            //updateUi(earthquake);
            ComicAdapter adapter = new ComicAdapter(SpiderManActivity.this,comics);

            ListView listView = (ListView)findViewById(R.id.listviewComic);
            listView.setAdapter(adapter);

        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, "Error with creating URL", exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            if(url == null){
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200){
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else{
                    Log.e(LOG_TAG, "Error response code: "+urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                // TODO: Handle the exception
                Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.",e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        //Metodo para trabajar con las llaves de Maervel
        private String enlaceMarvel(String enlaceBase){
            long ts = System.currentTimeMillis();
            StringBuilder cadena = new StringBuilder();
            cadena.append(ENLACE_BASE);
            cadena.append("ts="+String.valueOf(ts)+"&");
            cadena.append("apikey="+PUBLIC_KEY+"&");
            String hash = md5Java(ts+PRIVATE_KEY+PUBLIC_KEY);
            cadena.append("hash="+hash);
            return cadena.toString();
        }

    }

    //Metodo para obtener el hash requerido
    public static String md5Java(String message){
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            //converting byte array to Hexadecimal String
            StringBuilder sb = new StringBuilder(2*hash.length);
            for(byte b : hash){
                sb.append(String.format("%02x", b&0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex) {
            Log.e("Actividad","Error:"+ex);
        } catch (NoSuchAlgorithmException ex) {
            Log.e("Actividad","Error:"+ex);
        } return digest;
    }

}
