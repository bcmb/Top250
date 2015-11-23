package com.example.bcmb.top250;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity {
    private ProgressDialog pDialog;
    String movieJsonString = null;
    ArrayList<HashMap<String, String>> movieList = new ArrayList<>();
    private static final String MOVIE_NAME = "name";
    private static final String POSTER_URL = "posterUrl";
    private static final String YEAR = "year";
    private static final String MOVIE_ARRAY = "movie";
    private static final String MOVIE_DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetContacts().execute();
        ListView lv = getListView();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.myapifilms.com/imdb/top?start=1&end=2&token=452e19d8-0763-4b1e-8c52-a559b5d35fe8&format=json&data=0");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    movieJsonString = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    movieJsonString = null;
                }
                movieJsonString = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                movieJsonString = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                JSONObject jsonObj = new JSONObject(movieJsonString);
                JSONObject ja = jsonObj.getJSONObject(MOVIE_DATA);
                JSONArray mvs = ja.getJSONArray(MOVIE_ARRAY);

                for (int i = 0; i < mvs.length(); i++) {
                    JSONObject m = mvs.getJSONObject(i);
                    String name = m.getString(MOVIE_NAME);
                    String year = m.getString(YEAR);
                    String imgUrl = m.getString(POSTER_URL);
                    HashMap<String, String> movie = new HashMap<String, String>();
                    // adding each child node to HashMap key => value
                    movie.put(MOVIE_NAME, name);
                    movie.put(YEAR, year);
                    movie.put(POSTER_URL, imgUrl);
                    // adding contact to contact list
                    movieList.add(movie);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, movieList, R.layout.list_item, new String[] { MOVIE_NAME, YEAR}, new int[] { R.id.name, R.id.year});
            setListAdapter(adapter);
        }
    }
}

