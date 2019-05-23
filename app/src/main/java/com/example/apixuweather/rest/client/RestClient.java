package com.example.apixuweather.rest.client;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.apixuweather.utils.Const.I_TAG;

public abstract class RestClient {

    private Retrofit mRetrofit;
    protected GsonBuilder mGsonBuilder;

    RestClient() {
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttpClient())
                .baseUrl(getBaseUrl())
                .build();
    }

    private OkHttpClient createOkHttpClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> Log.d(I_TAG, message));
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    /** В этом методе производные классы добавляют кастомные десиреализаторы в qson*/
    abstract Gson createGson();

    public <S> S createService(Class<S> serviceClass){
        return mRetrofit.create(serviceClass);
    }

    protected abstract String getBaseUrl();
}
