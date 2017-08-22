package com.example.weijinqian.retrofit.network;

import com.example.weijinqian.retrofit.network.rx.CallBack;
import com.example.weijinqian.retrofit.network.rx.ICall;
import com.example.weijinqian.retrofit.network.rx.RxCall;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by weijinqian on 2017/8/18.
 */

public class NetworkUtil {
    public static <T> RxCall<T> getCall(final Call<T> call){
        return RxCall.build(new ICall() {
            @Override
            public void enqueue1(CallBack callBack) {
                call.enqueue(new Callback<T>() {
                    @Override
                    public void onResponse(Call<T> call, Response<T> response) {

                    }

                    @Override
                    public void onFailure(Call<T> call, Throwable t) {

                    }
                });
            }
        });
    }
}
