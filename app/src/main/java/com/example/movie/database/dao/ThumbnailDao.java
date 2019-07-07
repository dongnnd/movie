package com.example.movie.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.graphics.Bitmap;

import com.example.movie.model.Movie;
import com.example.movie.model.Thumbnail;

import java.util.List;

@Dao
public interface ThumbnailDao {
    @Query("SELECT bimaps FROM thumnails WHERE id = :id")
    byte[] getBitmap(int id);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertListPlan(Thumbnail thumbnail);
}
