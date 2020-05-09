package com.example.myapplication.http;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @description: socket的数据包的发送与接收拦截器
 * @author: chenjiayou
 * @createBy: 2020-5-9
 */

public class CallServiceInterceptor implements Interceptor {
    @Override
    public Response intercept(InterceptorChain interceptorChain) throws IOException {

        Log.e("interceptor", "通信拦截器");

        HttpConnection httpConnection = interceptorChain.httpConnection;
        HttpCodec httpCodec = new HttpCodec();
        InputStream inputStream = httpConnection.call(httpCodec);

        //获取服务器返回的响应行 HTTP/1.1 200 OK\r\n
        String statusLine = httpCodec.readLine(inputStream);
        Log.e("interceptor", "通信拦截器 status : " + statusLine);
        //获取服务器返回的响应头
        Map<String,String> headers = httpCodec.readHeaders(inputStream);
        Log.e("interceptor", "通信拦截器 headers : " + headers.toString());
        //根据Content-Length或者Transfer-Encoding(分块)计算响应体的长度
        int contentLength = -1;
        if (headers.containsKey(HttpCodec.HEAD_CONTENT_LENGTH)) {
            String length = headers.get(HttpCodec.HEAD_CONTENT_LENGTH);
            if (!TextUtils.isEmpty(length)){
                contentLength = Integer.parseInt(length);
            }
        }

        //是否为分块编码
        boolean isChunked = false;
        if (headers.containsKey(HttpCodec.HEAD_TRANSFER_ENCODING)) {
            isChunked = headers.get(HttpCodec.HEAD_TRANSFER_ENCODING).equalsIgnoreCase(HttpCodec.HEAD_VALUE_CHUNKED);
        }

        //获取服务器响应体
        String body = null;
        if (contentLength != -1){
            byte[] bytes = httpCodec.readBytes(inputStream,contentLength);
            body = new String(bytes, HttpCodec.ENCODE);
        }else if (isChunked){
            body = httpCodec.readChunked(inputStream, contentLength);
        }

        // HTTP/1.1 200 OK\r\n status[0] = "HTTP/1.1",status[1] = "200",status[2] = "OK\r\n"
        String[] status = statusLine.split(" ");

        //根据响应头中的Connection的值，来判断是否能够复用连接
        boolean isKeepAlive = false;
        if (headers.containsKey(HttpCodec.HEAD_CONNECTION)){
            isKeepAlive = headers.get(HttpCodec.HEAD_CONNECTION).equalsIgnoreCase(HttpCodec.HEAD_VALUE_KEEP_ALIVE);
        }

        //更新此请求的最新使用时间，作用于线程池的清理工作
        httpConnection.updateLastUseTime();
        return new Response(Integer.valueOf(status[1]), contentLength, headers, body, isKeepAlive);
    }
}
