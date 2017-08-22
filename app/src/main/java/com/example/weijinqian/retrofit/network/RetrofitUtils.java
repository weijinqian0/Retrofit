package com.example.weijinqian.retrofit.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by weijinqian on 2017/8/18.
 */

public class RetrofitUtils {
    /**
     * Retrofit实例
     */
    private static Retrofit retrofit;
    /**
     * 网络接口
     */
    private static volatile ApiService service;
    /**
     * 配置信息
     */
    private static Configuration configuration;

    /**
     * 获取相同baseUrl的默认网络接口
     *
     * @return 网络接口
     */
    public static ApiService getService() {
        if (configuration == null) {
            throw new AssertionError("configuration can not be null.");
        }

        if (service == null) {
            synchronized (RetrofitUtils.class) {
                if (service == null) {
                    service = getRetrofit().create(ApiService.class);
                }
            }
        }
        return service;
    }

    /**
     * 获取相同baseUrl，自定义参数的网络接口
     *
     * @param configuration 配置信息
     * @return 网络接口
     */
    public static ApiService getService(Configuration configuration) {
        if (configuration == null) {
            throw new AssertionError("configuration can not be null.");
        }

        return buildRetrofit(configuration).create(ApiService.class);
    }

    /**
     * 设置默认配置信息
     *
     * @param configuration 配置信息
     */
    public static void setConfiguration(Configuration configuration) {
        RetrofitUtils.configuration = configuration;
    }

    /**
     * 获得Retrofit实例
     *
     * @return Retrofit实例
     */
    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            initRetrofit();
        }
        return retrofit;
    }

    /**
     * 初始化Retrofit实例
     */
    private static void initRetrofit() {
        if (null != retrofit) {
            return;
        }

        retrofit = buildRetrofit(configuration);
    }

    /**
     * 根据配置信息构造Retrofit实例
     *
     * @param configuration 配置信息
     * @return Retrofit实例
     */
    private static Retrofit buildRetrofit(Configuration configuration) {
        // 网络缓存路径文件
        // File httpCacheDirectory = new File(BaseApplication.getInstance().getExternalCacheDir(), "responses");
        // 通过拦截器设置缓存，暂未实现
        // CacheInterceptor cacheInterceptor = new CacheInterceptor();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 设置默认的连接、读写的超时时间来解决部分手机写入操作超时后崩溃
        builder.writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(30, TimeUnit.SECONDS);
        // 设置缓存
        // .cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024))
        // log请求参数

        if (null != configuration.interceptors && 0 != configuration.interceptors.size()) {
            for (Interceptor interceptor : configuration.interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(configuration.baseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * 配置信息
     */
    public static class Configuration {
        /**
         * 是否使用测试地址
         */
        private Supplier<String> baseUrl;

        private List<Interceptor> interceptors;

        private Configuration(Supplier<String> baseUrl, List<Interceptor> interceptors) {
            this.baseUrl = baseUrl;
            this.interceptors = interceptors;
        }

        public static class Builder {
            private Supplier<String> baseUrl;
            private List<Interceptor> interceptors = new ArrayList<>();

            public Builder() {
            }

            public Builder baseUrl(Supplier<String> baseUrl) {
                this.baseUrl = baseUrl;
                return this;
            }

            public Builder addInterceptor(Interceptor interceptor) {
                this.interceptors.add(interceptor);
                return this;
            }

            public Configuration build() {
                return new Configuration(baseUrl, interceptors);
            }
        }
    }
}
