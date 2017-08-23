package com.example.weijinqian.retrofit.network;

/**
 * Created by weijinqian on 2017/8/23.
 */

public interface ProgressListener {
    /**
     * 进度变化
     * @param progress 当前进度
     * @param total    最长度
     * @param done     是否完成
     */
    void onProgress(long progress,long total,boolean done);
}
