package com.example.myapplication.http;

import android.util.Log;

import com.example.myapplication.MainActivity;

import java.io.IOException;

public class HeadInterceptor implements Interceptor {
    @Override
    public Response intercept(InterceptorChain interceptorChain) throws IOException {
        Log.d(MainActivity.TAG, "head : " + interceptorChain.index);
        Response response = interceptorChain.proceed();
        response.body += "\t head";
        return response;
    }
}
