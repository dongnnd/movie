package com.example.movie.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton mInstance;
    private RequestQueue mQueue;
    private static Context mContext;

    public VolleySingleton(Context context){
        mContext = context;
    }

    public synchronized static VolleySingleton getInstance(Context context){
        if(mInstance == null){
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mQueue == null){
            mQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
