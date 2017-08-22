package com.example.weijinqian.retrofit.network.rx;

import com.example.weijinqian.retrofit.Function;

import java.util.concurrent.Executor;
import java.util.concurrent.RunnableFuture;

import retrofit2.Call;

/**
 * Created by weijinqian on 2017/8/18.
 */

public class RxCall<T> {
    private ICall call;

    private RxCall(ICall call) {
        this.call = call;
    }

    public static <T> RxCall<T> build(ICall call) {
        return new RxCall<>(call);
    }

    public void enqueue(CallBack callBack) {

    }

    /**
     * @param executor
     * @return 在enqueue1中的callback是在调用enqueue1的时候传进去的，这里其实是将Icall中的Callback放到了
     * 赋值给RXCall
     */
    public RxCall<T> observeOn(final Executor executor) {
        final RxCall<T> rxCall = this;
        return RxCall.build(new ICall() {
            @Override
            public void enqueue1(final CallBack callBack) {
                rxCall.enqueue(new CallBack<T>() {
                    @Override
                    public void onSuc(final T response) {
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuc(response);
                            }
                        });
                    }

                    @Override
                    public void onFail(final int code, final String message) {
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFail(code,message);
                            }
                        });
                    }
                });
            }
        });

    }

    public <R> RxCall<R> mapping(final Function<T, R> function) {
        final RxCall<T> rxCall = this;
        return RxCall.build(new ICall() {
            @Override
            public void enqueue1(final CallBack callBack) {
//                可以直接调用callback 但是这样就没法封装了  所以必须得
//                rxCall.enqueue(callBack);
                rxCall.enqueue(new CallBack<T>() {
                    @Override
                    public void onSuc(T response) {

                    }

                    @Override
                    public void onFail(int code, String message) {

                    }
                });
            }
        });
    }
}
