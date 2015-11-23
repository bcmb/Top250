package com.example.bcmb.top250;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonParser {
    private static final String MOVIE_NAME = "title";
    private static final String POSTER_URL = "urlPoster";
    private static final String YEAR = "year";
    private static final String MOVIE_ARRAY = "movies";

    public static ArrayList<HashMap<String, String>> movieJSONParser(String jsonStr) throws JSONException {
        ArrayList<HashMap<String, String>> movieList = new ArrayList<>();
        JSONObject jsonObj = new JSONObject(jsonStr);
        JSONArray movies = jsonObj.getJSONArray(MOVIE_ARRAY);
        for (int i = 0; i < movies.length(); i++) {
            JSONObject m = movies.getJSONObject(i);
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
        return movieList;
    }
}
