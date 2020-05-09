package com.example.myapplication.http;

import android.util.Log;

import java.io.IOException;

/**
 * @description: 获得有效连接的socket的拦截器
 * @author: chenjiayou
 * @createBy: 2020-5-9
 */

public class ConnectionInterceptor implements Interceptor {
    @Override
    public Response intercept(InterceptorChain interceptorChain) throws IOException {
        Log.e("interceptor", "获取连接拦截器");

        Request request = interceptorChain.call.getRequest();

        HttpClient httpClient = interceptorChain.call.getHttpClient();
        HttpUrl httpUrl = request.getHttpUrl();
        HttpConnection httpConnection = httpClient.getConnectionPool().getHttpConnection(httpUrl.host,httpUrl.port);
        if (null == httpConnection){
            httpConnection = new HttpConnection();
        }else{
            Log.e("interceptor", "从连接池中获得连接");
        }
        httpConnection.setRequest(request);

        try {
            Response response = interceptorChain.proceed(httpConnection);
            if (response.isKeepAlive()){
                httpClient.getConnectionPool().putHttpConnection(httpConnection);
            }else{
                httpConnection.close();
            }
            return response;
        }catch (IOException e){
            httpConnection.close();
            throw e;
        }
    }
}
