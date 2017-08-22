package com.example.weijinqian.retrofit;

/**
 * Created by weijinqian on 2017/8/18.
 */

public interface Function<T,R> {
    R apply(T t);
}
