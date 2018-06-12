package dev.at.mikorn.vn.service;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author Mikorn vietnam
 * Created on 12-Jun-18.
 */

public class RxJavaCallAdapter {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(IfpAPI.SERVER_CONFIG.IFP.getUrlEndPoit())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
}
