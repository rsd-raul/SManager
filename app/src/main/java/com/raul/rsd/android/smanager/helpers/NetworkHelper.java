/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.raul.rsd.android.smanager.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

import com.raul.rsd.android.smanager.domain.Resource;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public abstract class NetworkHelper {

    private static final String TAG = "NetworkHelper";

    // --------------------------- VALUES ----------------------------

    private static final String PEACHES = "Peaches";
    private static final String FRUIT = "Fruit";
    private static final String FILE = "hma6-9xbg.json";

    private static final String BASE_DATA_CT_URL = "http://data.ct.gov/";
//    private static final String BASE_DATA_CT_URL = "https://data.ct.gov/"; // TODO: Server failure
// javax.net.ssl.SSLException: SSL handshake aborted: ssl=0x7b2d9a38: I/O error during system call, Connection reset by peer


    private static final String CATEGORY_PARAM = "category";
    private static final String ITEM_PARAM = "item";

    // -------------------------- RETROFIT 2 -------------------------

    interface DataCtService {

        //https://data.ct.gov/resource/hma6-9xbg.json?category=Fruit&item=Peaches
        @GET("resource/{file}")
        Call<List<Resource>> getResource(@Path("file") String file,
                                         @Query(CATEGORY_PARAM) String category,
                                         @Query(ITEM_PARAM) String item);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_DATA_CT_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .client(provideCustomClient())
                .build();
    }

//    private static OkHttpClient provideCustomClient() {
//        return new OkHttpClient.Builder()
//                .readTimeout(60, TimeUnit.SECONDS)
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .connectionPool(new ConnectionPool(100,10000, TimeUnit.SECONDS))
//                .build();
//    }

    private static NetworkHelper.DataCtService getDataCtService(){
        return NetworkHelper.DataCtService.retrofit.create(NetworkHelper.DataCtService.class);
    }

    // --------------------------- NETWORK ---------------------------

    public static void getRequestedResource(Callback<List<Resource>> callback){
        getDataCtService().getResource(FILE, FRUIT, PEACHES).enqueue(callback);
    }

    // -------------------------- AUXILIARY --------------------------

    /**
     * Check whether the device is connected to the internet or not
     *
     * @param activity The activity you wish to check from
     * @return true if the device is connected, false otherwise
     */
    public static boolean isNetworkAvailable(AppCompatActivity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}