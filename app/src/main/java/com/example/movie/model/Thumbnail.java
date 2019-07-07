package com.example.movie.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

@Entity(tableName = "thumnails")
public class Thumbnail {

    @PrimaryKey
    private int id;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] bimaps;

    public Thumbnail(int id, byte[] bimaps){
        this.id = id;
        this.bimaps = bimaps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getBimaps() {
        return bimaps;
    }

    public void setBimaps(byte[] bimaps) {
        this.bimaps = bimaps;
    }
}
