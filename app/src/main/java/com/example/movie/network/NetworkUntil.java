package com.example.movie.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.movie.R;
import com.example.movie.model.Movie;
import com.example.movie.uitil.AppContants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class NetworkUntil {

    private static final String URL_TOP_RATE = "http://api.themoviedb.org/3/movie/top_rated?api_key=";
    private static final String URL_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=";
    private static final String URL_UPCOMING = "http://api.themoviedb.org/3/movie/upcoming?api_key=";
    private static final String URL_SEARCH = "https://api.themoviedb.org/3/search/movie?api_key=5f52f0db3d481dc34ba444bf202c5120&query=";
    private static final String URL_BASE = "https://image.tmdb.org/t/p/";
    private static final String KEY = "5f52f0db3d481dc34ba444bf202c5120";

    public static void getMovieFromServer(int type, Context context, final IVolleyResultCallBack callBack) {
        String url = "";
        switch (type) {
            case AppContants.MovieType.POPULAR:
                url = URL_POPULAR;
                break;
            case AppContants.MovieType.TOP_RATE:
                url = URL_TOP_RATE;
                break;
            case AppContants.MovieType.UPCOMMING:
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
                callBack.loadFail(getVolleyErrorType(error));
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static List<Movie> parseJson(JSONArray jsonArray, int movieType) {
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
                movie.setMovieType(movieType);
                lists.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("dong.nd1", "Error occurred during JSON Parsing");
        }
        return lists;
    }

    public static Bitmap getThumbnailFromUrl(String sUrl){
        String url = URL_BASE +"w500"+ sUrl;
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            Log.d("dong.nd1", e.toString());
            return null;
        }
    }
    public static int getVolleyErrorType(VolleyError volleyError){
        int typeError = 1;
        if (volleyError instanceof NetworkError) {
            typeError = AppContants.VooleyError.NetworkError;
            //message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            typeError = AppContants.VooleyError.ServerError;
            //message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            typeError = AppContants.VooleyError.AuthFailureError;
            // message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ParseError) {
            typeError = AppContants.VooleyError.ParseError;
            //message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof NoConnectionError) {
            typeError = AppContants.VooleyError.NoConnectionError;
            // message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof TimeoutError) {
            typeError = AppContants.VooleyError.TimeoutError;
            // message = "Connection TimeOut! Please check your internet connection.";
        }

        return typeError;
    }


    public static String getVolleyErrorMessage(int type){
        String message ="";if (type == AppContants.VooleyError.NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (type == AppContants.VooleyError.ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (type == AppContants.VooleyError.AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (type == AppContants.VooleyError.ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (type == AppContants.VooleyError.NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (type == AppContants.VooleyError.TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        return message;
    }

    public static void searchInServer(final String str, Context context, final IVolleyResultCallBack callBack){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_SEARCH+str, null, new Response.Listener<JSONObject>() {
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
                callBack.loadFail(getVolleyErrorType(error));
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }

        return isConnected;
    }

    public static void showToastErrorNetwork(Context context){
        //Toast.makeText(context.getApplicationContext(), R.string.network_error, Toast.LENGTH_LONG).show();
    }
}
