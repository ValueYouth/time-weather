package com.valueyouth.timeweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 类说明：网络连接。
 * Created by cwang6 on 2017/1/3.
 */

public class HttpUtil
{
    public static void sendOKHttpRequest(String address, okhttp3.Callback callback)
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();

        client.newCall(request).enqueue(callback);
    }
}
