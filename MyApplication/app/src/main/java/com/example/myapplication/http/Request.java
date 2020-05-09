package com.example.myapplication.http;

import androidx.annotation.NonNull;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private Map<String, String> headers;//http包请求头
    private String method; //请求方法 get,post,delete 等
    private HttpUrl httpUrl; //请求url信息
    private RequestBody requestBody; //请求体 如果是post请求，还会有requestBody存参数信息

    private Request(Builder builder){
        this.headers = builder.headers;
        this.method = builder.method;
        this.httpUrl = builder.httpUrl;
        this.requestBody = builder.requestBody;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method;
    }

    public HttpUrl getHttpUrl() {
        return httpUrl;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public static final class Builder {
        private Map<String, String> headers = new HashMap<>();//http包请求头
        private String method; //请求方法 get,post,delete 等
        private HttpUrl httpUrl; //请求url信息
        private RequestBody requestBody; //请求体 如果是post请求，还会有requestBody存参数信息

        public Builder addHeader(String key, String value) {
            this.headers.put(key,value);
            return this;
        }

        public Builder removeHeader(String key) {
            this.headers.remove(key);
            return this;
        }

        public Builder get() {
            this.method = HttpCodec.GET;
            return this;
        }

        public Builder post(RequestBody requestBody){
            this.requestBody = requestBody;
            this.method = "POST";
            return this;
        }

        public Builder withHttpUrl(@NonNull String url) {
            try {
                this.httpUrl = new HttpUrl(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Builder withRequestBody(RequestBody requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public Request build() {
            if(null == httpUrl){
                throw new IllegalStateException("url is null!");
            }
            if(null == method){
                method = "GET";
            }
            return new Request(this);
        }
    }
}
