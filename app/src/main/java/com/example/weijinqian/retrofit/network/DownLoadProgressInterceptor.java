package com.example.weijinqian.retrofit.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by weijinqian on 2017/8/23.
 */

class DownLoadProgressInterceptor implements Interceptor {
    public DownLoadProgressInterceptor(ProgressListener progressListener) {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return null;
    }
}
