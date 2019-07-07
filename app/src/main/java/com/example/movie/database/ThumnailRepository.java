package com.example.movie.database;

import android.graphics.Bitmap;

import com.example.movie.database.dao.ThumbnailDao;
import com.example.movie.model.Thumbnail;

public class ThumnailRepository {

    public static ThumnailRepository mInstances;
    private ThumbnailDao mTmbDao;

    public  ThumnailRepository(ThumbnailDao tmbDao){
        mTmbDao = tmbDao;
    }

    public static ThumnailRepository getInstance(ThumbnailDao tmbDao){
        if(mInstances == null){
            mInstances = new ThumnailRepository(tmbDao);
        }

        return mInstances;
    }


    public void insertTmb(Thumbnail thumbnail){
        mTmbDao.insertListPlan(thumbnail);
    }

    public byte[] getThumnail(int id){
        return mTmbDao.getBitmap(id);
    }
}
