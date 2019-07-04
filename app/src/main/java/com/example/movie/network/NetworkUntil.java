package com.example.movie.network;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.movie.model.Movie;
import com.example.movie.uitil.AppContants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NetworkUntil {

    private static final String URL_TOP_RATE = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=";
    private static final String URL_POPULAR = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";
    private static final String URL_UPCOMING = "";
    private static final String KEY = "5f52f0db3d481dc34ba444bf202c5120";

    public static void getMovieFromServer(AppContants.MovieType type, Context context, final IVolleyResultCallBack callBack) {
        String url = "";
        switch (type) {
            case POPULAR:
                url = URL_POPULAR;
                break;
            case TOP_RATE:
                url = URL_TOP_RATE;
                break;
            case UPCOMMING:
                url = URL_UPCOMING;
                break;
        }
        url += KEY;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    callBack.loadSuccess(jsonArray);
                } catch (JSONException e) {
                    Log.d("dong.nd1", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.loadFail(error.getMessage());
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static List<Movie> parseJson(JSONArray jsonArray) {
        List<Movie> lists = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setIdMovie(jsonObject.getInt("id"));
                movie.setVoteAverage(jsonObject.getInt("vote_average"));
                movie.setVoteCount(jsonObject.getInt("vote_count"));
                movie.setOriginalTitle(jsonObject.getString("original_title"));
                movie.setTitle(jsonObject.getString("title"));
                movie.setPopularity(jsonObject.getDouble("popularity"));
                movie.setBackdropPath(jsonObject.getString("backdrop_path"));
                movie.setOverview(jsonObject.getString("overview"));
                movie.setReleaseDate(jsonObject.getString("release_date"));
                movie.setPosterPath(jsonObject.getString("poster_path"));
                lists.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("dong.nd1", "Erro occurred during JSON Parsing");
        }
        return lists;
    }
}