package com.example.movie.network;

import com.example.movie.model.Movie;

import org.json.JSONArray;

import java.util.List;

public interface IVolleyResultCallBack {
    void loadSuccess(JSONArray jsonArray);
    void loadFail(int errorType);
}
