package com.example.movie.viewmodel;

import com.example.movie.model.Movie;

import java.util.List;

public interface IDataCallBack {
    void loadSuccess(List<Movie> listMovie);
    void loadFail(String error);
}
