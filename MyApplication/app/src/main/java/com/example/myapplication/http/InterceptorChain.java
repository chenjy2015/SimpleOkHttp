package com.example.myapplication.http;

import java.io.IOException;
import java.util.List;

public class InterceptorChain {

    List<Interceptor> interceptors;
    final int index;
    final Call call;
    HttpConnection httpConnection;

    public InterceptorChain(List<Interceptor> interceptors, int index, Call call, HttpConnection httpConnection) {
        this.interceptors = interceptors;
        this.index = index;
        this.call = call;
        this.httpConnection = httpConnection;
    }
    public Response proceed(HttpConnection httpConnection) throws IOException{
        this.httpConnection = httpConnection;
        return proceed();
    }
    public Response proceed() throws IOException {
        if(index > interceptors.size()){
            throw new IOException("Interceptor Chain Error");
        }
        Interceptor interceptor = interceptors.get(index);
        InterceptorChain next = new InterceptorChain(interceptors,index + 1, call, httpConnection);
        return interceptor.intercept(next);
    }

}
