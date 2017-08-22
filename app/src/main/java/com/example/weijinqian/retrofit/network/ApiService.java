package com.example.weijinqian.retrofit.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by weijinqian on 2017/8/18.
 */

public interface ApiService {
    @GET
    public Call<ResponseBody> getFirstThing(@Query("name") String name);
}
