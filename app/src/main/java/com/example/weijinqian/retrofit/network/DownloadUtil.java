package com.example.weijinqian.retrofit.network;

import com.example.weijinqian.retrofit.network.rx.CallBack;
import com.example.weijinqian.retrofit.network.rx.ICall;
import com.example.weijinqian.retrofit.network.rx.RxCall;
import com.example.weijinqian.retrofit.network.rx.RxUtils;

import okhttp3.ResponseBody;

/**
 * Created by weijinqian on 2017/8/23.
 */

public class DownloadUtil {
    public static RxCall<Boolean> getDownload(final ResponseBody response,
                                              final String targetPath,
                                              final Boolean append){
        return RxCall.build(new ICall<Boolean>() {
            @Override
            public void enqueue1(final CallBack<Boolean> callback) {
                RxUtils.getWorkExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuc(writeResponseBodyToDisk(response,targetPath,append));
                    }
                });
            }
        });
    }

    private static Boolean writeResponseBodyToDisk(ResponseBody response, String targetPath, Boolean append) {
        return false;
    }
}
