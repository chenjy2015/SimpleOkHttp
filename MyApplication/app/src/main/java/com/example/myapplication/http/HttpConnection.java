package com.example.myapplication.http;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

public class HttpConnection {
    Socket socket;
    long lastUseTime;
    private Request request;
    private InputStream inputStream;
    private OutputStream outputStream;

    public void setRequest(Request request) {
        this.request = request;
    }

    public void updateLastUseTime() {
        this.lastUseTime = System.currentTimeMillis();
    }

    /**
     * 对比host 和端口号 判断是否同一个地址
     *
     * @param host
     * @param port
     * @return
     */
    public boolean isSameAddress(String host, int port) {
        if (null == socket) {
            return false;
        }
        return TextUtils.equals(request.getHttpUrl().getHost(), host) && request.getHttpUrl().port == port;
    }

    private void createSocket() throws IOException {
        if (null == socket || socket.isClosed()) {
            HttpUrl httpUrl = request.getHttpUrl();
            if (httpUrl.protocol.equalsIgnoreCase(HttpCodec.PROTOCOL_HTTPS)) {
                //如果是https，就需要使用jdk默认的SSLSocketFactory来创建socket
                socket = SSLSocketFactory.getDefault().createSocket();
            } else {
                socket = new Socket();
            }
            socket.connect(new InetSocketAddress(httpUrl.getHost(), httpUrl.getPort()));
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        }
    }

    public void close() {
        if (null != socket) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public InputStream call(HttpCodec httpCodec) throws IOException {
        //创建socket
        createSocket();
        httpCodec.writeRequest(outputStream, request);
        return inputStream;
    }
}
