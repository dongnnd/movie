package com.example.movie.uitil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.example.movie.database.AppDatabase;
import com.example.movie.database.MovieRepository;
import com.example.movie.database.ThumnailRepository;
import com.example.movie.database.dao.ThumbnailDao;
import com.example.movie.model.Movie;
import com.example.movie.model.Thumbnail;
import com.example.movie.network.NetworkUntil;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.Callable;

public class ThumbnailMrg {

    private static ThumbnailMrg mInstances;

    private Handler mUiHandler;
    private ThumnailRepository mTmbRepositoy;
    private MovieRepository movieRepository;

    public ThumbnailMrg(Context context){

        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        mTmbRepositoy = ThumnailRepository.getInstance(database.getThumbnailDao());
        movieRepository = MovieRepository.getInstance(database.getMovieDao());

        mUiHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                CustomObject object = (CustomObject) msg.obj;
                (object.refImage.get()).setImageBitmap(object.bitmap);
                super.handleMessage(msg);
            }
        };
    }

    public static ThumbnailMrg getInstance(Context context){
        if(mInstances == null){
            mInstances = new ThumbnailMrg(context);
        }
        return mInstances;
    }

    public void loadThumbnail(final int id, final String url, final ImageView view){
        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                Bitmap bitmap = null;
                 byte[] bitmapArray = mTmbRepositoy.getThumnail(id);


                if(bitmapArray != null){
                    Log.d("dong.nd1", "Jump1");
                    bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
                }
                if(bitmap == null){
                    bitmap = NetworkUntil.getThumbnailFromUrl(url);

                    if(bitmap != null){
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        byte[] bArray = bos.toByteArray();
                        mTmbRepositoy.insertTmb(new Thumbnail(id, bArray));
                    }
                }

                if(bitmap != null){

                    Message msg = mUiHandler.obtainMessage();
                    msg.obj = new CustomObject(bitmap, new WeakReference<>(view));
                    mUiHandler.sendMessageAtFrontOfQueue(msg);

                }
                return null;
            }
        };
        ThreadExecutor.getInstance().addTaskLoadThumbnail(callable);
    }

    class CustomObject extends Object{
        public Bitmap bitmap;
        private WeakReference<ImageView> refImage;

        public CustomObject(Bitmap bitmap, WeakReference<ImageView> refImage){
            this.bitmap = bitmap;
            this.refImage = refImage;
        }
    }

}
