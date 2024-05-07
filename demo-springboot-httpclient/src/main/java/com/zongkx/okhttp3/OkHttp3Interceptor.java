package com.zongkx.okhttp3;

import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@RequiredArgsConstructor
public  class OkHttp3Interceptor implements Interceptor {
    private final OkHttpClient okHttpClient;

    @Override
    public Response intercept( Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Request request = new Request.Builder().get().url("http://localhost:8080/get?name=aaaa").build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            builder.addHeader("token", "test");
            builder.addHeader("Content-Type", "application/json");
            return chain.proceed(builder.build());
        }
    }
}