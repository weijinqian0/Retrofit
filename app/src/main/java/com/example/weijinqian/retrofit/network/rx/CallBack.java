package com.example.weijinqian.retrofit.network.rx;

/**
 * Created by weijinqian on 2017/8/18.
 */

public interface CallBack<T> {
    public void onSuc(T response);
    public void onFail(int code ,String message);
}
