package com.example.weijinqian.retrofit.network;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weijinqian on 2017/8/23.
 */

public class Header {
    /**
     * UA
     */
    private static final String USER_AGENT = "User-Agent";
    /**
     * 断点续传
     */
    private static final String HEADER_RANGE = "RANGE";

    public static class Builder {
        /*防止map指向对象发生变化*/
        private final HashMap<String, String> map;

        public Builder() {
            this.map = new HashMap<>();
        }

        public Builder append(String key, String value) {
            map.put(key, value);
            return this;
        }

        public Builder range(long start) {
            map.put(HEADER_RANGE, "bytes=" + start + "-");
            return this;
        }

        public Builder range(long start, long end) {
            map.put(HEADER_RANGE, "bytes=" + start + "-" + end);
            return this;
        }

        public Builder userAgent(String userAgent) {
            map.put(USER_AGENT, userAgent);
            return this;
        }
        /*HashMap的clone方法是类似浅clone，这就导致在对HashMap中的元素进行修改的时候，
        即对数组中元素进行修改，会导致原对象和clone对象都发生改变，但进行新增或删除就不会影响对方*/
        public Map<String, String> build() {
            return (HashMap<String, String>) map.clone();
        }
    }
}
