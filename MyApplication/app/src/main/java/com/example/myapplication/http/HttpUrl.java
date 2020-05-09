package com.example.myapplication.http;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class HttpUrl {

    String protocol;
    String host;
    String file;
    int port;

    public HttpUrl(String url) throws MalformedURLException {
        URL localUrl = new URL(url);
        protocol = localUrl.getProtocol();
        host = localUrl.getHost();
        file = localUrl.getFile();
        port = localUrl.getPort();
        if (port == -1) {
            //代表url中没有端口信息，就是使用默认端口，http:80,https:443
            port = localUrl.getDefaultPort();
        }
        if (TextUtils.isEmpty(file)) {
            //如果为空，默认加上"/"
            file = "/";
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getFile() {
        return file;
    }

    public int getPort() {
        return port;
    }
}
