package com.example.networkpractice.retrofit;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCall {
    private static final String BASE_URL= "https://jsonplaceholder.typicode.com/";
    private static Retrofit retrofit = null;
    public static Retrofit makeApiCall() {
        if(retrofit == null) {
            Log.e("Connect", "Retrofit instance created");
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
