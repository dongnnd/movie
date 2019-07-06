package com.example.movie.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.movie.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie WHERE movieType = :type")
    List<Movie> getListMovieType(int type);

    @Query("DELETE FROM movie WHERE movieType = :type")
    int deleteListMovieType(int type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertListPlan(List<Movie> lists);
}
