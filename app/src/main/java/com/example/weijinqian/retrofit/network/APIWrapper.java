package com.example.weijinqian.retrofit.network;

import com.example.weijinqian.retrofit.network.rx.RxCall;

import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.example.weijinqian.retrofit.network.NetworkUtil.getCall;

/**
 * Created by weijinqian on 2017/8/18.
 */

public class APIWrapper {
    public static RxCall<ResponseBody> getFirstThing(String name){
        Call<ResponseBody>  call=RetrofitUtils.getService().getFirstThing(name);
        return getCall(call);
    }
}
