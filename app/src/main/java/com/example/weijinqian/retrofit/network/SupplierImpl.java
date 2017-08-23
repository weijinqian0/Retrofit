package com.example.weijinqian.retrofit.network;

import android.support.annotation.NonNull;

/**
 * Created by weijinqian on 2017/8/23.
 */

public class SupplierImpl {
    public static final boolean IS_PREVIEW_URL=false;
    public static final boolean IS_TEST_URL=true;

    @NonNull
    public static Supplier<String> getBaseUrl() {
        return new Supplier<String>() {
            @Override
            public String get() {
                if (IS_PREVIEW_URL) {
                    return "https://adada/";
                } else if (IS_TEST_URL) {
                    return "https://asdasdas/";
                } else {
                    return "https://adadad/";
                }
            }
        };
    }
}
