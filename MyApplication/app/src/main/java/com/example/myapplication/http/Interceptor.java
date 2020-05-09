package com.example.myapplication.http;

import java.io.IOException;

public interface Interceptor {
    Response intercept(InterceptorChain interceptorChain) throws IOException;
}
