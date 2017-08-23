package com.example.weijinqian.retrofit.network;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by weijinqian on 2017/8/18.
 */

public interface ApiService {
    @GET
    public Call<ResponseBody> getFirstThing(@Query("name") String name);
    @GET
    public Call<ResponseBody> downloadFile(@Url String url, @HeaderMap Map<String,String> header);
}
