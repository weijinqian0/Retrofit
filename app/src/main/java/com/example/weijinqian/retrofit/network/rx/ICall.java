package com.example.weijinqian.retrofit.network.rx;

/**
 * Created by weijinqian on 2017/8/18.
 */

public interface ICall<T> {
    abstract void enqueue1(CallBack<T> callback);
}
