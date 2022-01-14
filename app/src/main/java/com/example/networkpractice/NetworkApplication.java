package com.example.networkpractice;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.networkpractice.retrofit.ApiCall;
import com.example.networkpractice.retrofit.JsonPlaceHolderApi;
import com.example.networkpractice.solution.ConnectionLiveData;

import retrofit2.Retrofit;

public class NetworkApplication  extends Application {
    private static Context application = null;
    private static JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Connect","Application Instance created");
        ConnectionLiveData connectionLiveData = new ConnectionLiveData(getApplicationContext());
        connectionLiveData.checkNetworkState();

        Retrofit retrofit = ApiCall.makeApiCall();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        application = this;
    }
    public static Context getApplication() {
        if(application == null)
            return  new NetworkApplication();
        return application;
    }

    public static JsonPlaceHolderApi getJsonPlaceHolderApi() {
        return jsonPlaceHolderApi;
    }

}
