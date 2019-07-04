package com.example.movie.network;

import org.json.JSONArray;

public interface IVolleyResultCallBack {
    void loadSuccess(JSONArray jsonArray);
    void loadFail(String error);
}
