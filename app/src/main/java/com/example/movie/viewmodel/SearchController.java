package com.example.movie.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.example.movie.model.Movie;
import com.example.movie.network.IVolleyResultCallBack;
import com.example.movie.network.NetworkUntil;
import com.example.movie.uitil.ThreadExecutor;

import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.Callable;

public class SearchController extends AndroidViewModel implements IVolleyResultCallBack {

    private Context mContext;
    public static final int QUERY_UPDATE_TIME = 300;
    private MutableLiveData<List<Movie>> mSearchList = new MutableLiveData<>();
    private MutableLiveData<String> mSeachText = new MutableLiveData<>();
    private MutableLiveData<Integer> mSearchErrorType = new MutableLiveData<>();

    public SearchController(Application application){
        super(application);
        mContext = application;
    }

    public void search(String str){
        if(str.equals("") || str == null){
            mSearchList.setValue(null);
            return;
        }
        NetworkUntil.searchInServer(str, mContext, this);
    }

    @Override
    public void loadSuccess(final JSONArray jsonArray) {
        Callable parseJson = new Callable() {
            @Override
            public Object call() throws Exception {
                List<Movie> listMovie = NetworkUntil.parseJson(jsonArray, -1);
                mSearchList.postValue(listMovie);
                if(listMovie.size() == 0){
                    Log.d("dong.nd1", "A");
                    mSearchErrorType.setValue(1);
                }
                return null;
            }
        };

        ThreadExecutor.getInstance().addTaskLoadMovie(parseJson);
    }

    @Override
    public void loadFail(int errorType) {
        Log.d("dong.nd1", "BBB");
        mSearchErrorType.setValue(errorType);
    }

    public LiveData<List<Movie>> getSearchList(){
        return mSearchList;
    }

    public LiveData<String> getSearchText(){
        return mSeachText;
    }

    public LiveData<Integer> getSearchErrorType(){
        return mSearchErrorType;
    }

    public void setSearchErrorType(int type){
        mSearchErrorType.setValue(type);
    }

    public void setSearchText(String str){
        mSeachText.setValue(str);
    }

    public void setSearchList(List<Movie> list){
        mSearchList.setValue(null);
    }

    public void refreshData(){
        mSearchList.setValue(null);
    }
}



