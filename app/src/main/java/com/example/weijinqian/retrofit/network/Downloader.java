package com.example.weijinqian.retrofit.network;

import android.os.Build;
import android.text.TextUtils;

import com.example.weijinqian.retrofit.Function;
import com.example.weijinqian.retrofit.network.rx.CallBack;
import com.example.weijinqian.retrofit.network.rx.ICall;
import com.example.weijinqian.retrofit.network.rx.RxCall;
import com.example.weijinqian.retrofit.network.rx.RxUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.ResponseBody;

import static com.example.weijinqian.retrofit.network.DownloadUtil.getDownload;

/**
 * Created by weijinqian on 2017/8/23.
 */

public class Downloader {
    //    是否附加设备信息
    private boolean deviceInfo;
    /*下载进度接口*/
    private ProgressListener progressListener;
    /*是否支持断点续传*/
    private boolean append;
    /*是否设置代理*/
    private boolean userAgent;
    /*是否先休眠，旧网络遗留，暂时不要实现*/
    private boolean needSleep;
    /*使用缓存*/
    private boolean userCache;
    /*缓存文件*/
    private File cacheFile;
    /*下载资源url*/
    private String url;
    /*文件存放*/
    private File targetFile;
    /*md5,用来校验文件*/
    private String md5;

    private Downloader() {

    }

    public static class Result {
        private final boolean success;
        private final String url;
        private final File targetFile;

        public Result(boolean success, String url, File targetFile) {
            this.success = success;
            this.url = url;
            this.targetFile = targetFile;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getUrl() {
            return url;
        }

        public File getTargetFile() {
            return targetFile;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "success=" + success +
                    ", url='" + url + '\'' +
                    ", targetFile=" + targetFile +
                    '}';
        }
    }

    public static class Builder {
        private boolean deviceInfo;
        /*下载进度接口*/
        private ProgressListener progressListener;
        /*是否支持断点续传*/
        private boolean append;
        /*是否设置代理*/
        private boolean userAgent;
        /*是否先休眠，旧网络遗留，暂时不要实现*/
        private boolean needSleep;
        /*使用缓存*/
        private boolean userCache;
        /*缓存文件*/
        private File cacheFile;
        /*下载资源url*/
        private String url;
        /*文件存放*/
        private File targetFile;
        /*md5,用来校验文件*/
        private String md5;

        public Builder deviceInfo(boolean deviceInfo) {
            this.deviceInfo = deviceInfo;
            return this;
        }

        public Builder progressListener(ProgressListener progressListener) {
            this.progressListener = progressListener;
            return this;
        }

        public Builder append(Boolean append) {
            this.append = append;
            return this;
        }

        public Builder userAgent(boolean userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder userCache(boolean userCache) {
            this.userCache = userCache;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder targetFile(File targetFile) {
            this.targetFile = targetFile;
            return this;
        }

        public Builder md5(String md5) {
            this.md5 = md5;
            return this;
        }

        public Downloader build() {
            Downloader downloader = new Downloader();
            downloader.deviceInfo = deviceInfo;
            downloader.append = append;
            downloader.cacheFile = cacheFile;
            downloader.progressListener = progressListener;
            downloader.userAgent = userAgent;
            downloader.needSleep = needSleep;
            downloader.cacheFile = cacheFile;
            downloader.url = url;
            downloader.targetFile = targetFile;
            downloader.md5 = md5;
            return downloader;
        }
    }

    public RxCall<Result> getDownloadCall(){
        final File file=userCache?cacheFile:targetFile;
        RxCall<Boolean> call=getReadFileCall(makeResponseBodyCall(),file.getAbsolutePath(),isAppend());
        if (userCache){
            call=call.flatmapping(new Function<Boolean, RxCall<Boolean>>() {
                @Override
                public RxCall<Boolean> apply(Boolean downloaded) {
                    if (!downloaded){
                        return getReturnCall(false);
                    }
                    if(!TextUtils.isEmpty(md5)&&!verifyFile(file,md5)){
                        return getReturnCall(false);
                    }
                    return copyFileCall(cacheFile,targetFile);
                }
            });
        }
        return call.mapping(new Function<Boolean, Result>() {
            @Override
            public Result apply(Boolean success) {
                if (userCache){
                    FileUtil.delete(cacheFile);
                }
                return new Result(success,url,targetFile);
            }
        });
    }

    private RxCall<Boolean> copyFileCall(final File srcFile, final File destFile) {
        return RxCall.build(new ICall<Boolean>() {
            @Override
            public void enqueue1(final CallBack<Boolean> callback) {
                RxUtils.getWorkExecutor().execute(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            FileUtil.copy(srcFile, destFile);
                            callback.onSuc(true);
                        } catch (IOException e){
                            e.printStackTrace();
                            callback.onSuc(false);
                        }
                    }
                });
            }
        });
    }

    private boolean verifyFile(File file, String token) {
        return file != null
                && (token == null || token.equalsIgnoreCase(SysInfo.md5String(SysInfo.md5Data(file))));
    }

    private RxCall<Boolean> getReturnCall(final Boolean v) {
        return RxCall.build(new ICall<Boolean>() {
            @Override
            public void enqueue1(CallBack<Boolean> callback) {
                callback.onSuc(v);
            }
        });
    }

    private RxCall<Boolean> getReadFileCall(final RxCall<ResponseBody> responseBodyRxCall, final String absolutePath, final boolean append) {
        return responseBodyRxCall.flatmapping(new Function<ResponseBody, RxCall<Boolean>>() {
            @Override
            public RxCall<Boolean> apply(ResponseBody body) {
                return getDownload(body,absolutePath,append);
            }
        });
    }

    /**
     * 在这里才做了网络请求，其他的都没有，只是封装操作
     * @return
     */
    private RxCall<ResponseBody> makeResponseBodyCall() {
        ApiService apiService=makeApiService();
        Map<String,String> header=makeHeader();
        return NetworkUtil.getCall(apiService.downloadFile(url,header));
    }

    private Map<String,String> makeHeader() {
        Header.Builder builder=new Header.Builder();
        if (isAppend()) {
            builder.range(targetFile.length());
        }

        if (userAgent) {
//            String packageName = TextUtils.isEmpty(Global.packageName)
//                    ? Global.getImeApp().getPackageName() : Global.packageName;
//            String versionName = TextUtils.isEmpty(Global.verName)
//                    ? Global.getInstalledPkgInfo(Global.packageName, 0).versionName : Global.verName;
//            String ua = String.format(Global.getImeApp().getString(R.string.user_agent_value),
//                    packageName, versionName, Build.VERSION.RELEASE, Build.MANUFACTURER, Build.ID);
//            builder.userAgent(ua);
        }

        return builder.build();
    }

    private ApiService makeApiService() {
        ApiService apiService;
        RetrofitUtils.Configuration.Builder builder;
        if (!deviceInfo){
            builder=new RetrofitUtils.Configuration.Builder().baseUrl(SupplierImpl.getBaseUrl());
            if (null!=progressListener){
                builder.addInterceptor(new DownLoadProgressInterceptor(progressListener));
            }
            apiService=RetrofitUtils.getService(builder.build());

        } else if (null!=progressListener){
            RetrofitUtils.Configuration configuration = RetrofitUtils.getConfiguration();
            builder = new RetrofitUtils.Configuration.Builder();
            builder.baseUrl(configuration.getBaseUrl());
            for (Interceptor interceptor : configuration.getInterceptors()) {
                builder.addInterceptor(interceptor);
            }
            builder.addInterceptor(new DownLoadProgressInterceptor(progressListener));
            apiService = RetrofitUtils.getService(builder.build());
        }else{
            apiService=RetrofitUtils.getService();
        }
        return apiService;
    }

    public boolean isAppend() {
        return append;
    }
}
