package com.example.myapplication.http;

import java.util.ArrayList;
import java.util.List;

public class HttpClient {
    //设置调度器
    private Dispatcher dispather;

    private List<Interceptor> interceptors;

    private int retryTimes;

    private ConnectionPool connectionPool;

    public Dispatcher getDispather() {
        return dispather;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    /**
     * 构造方法
     */
    public HttpClient(Builder builder) {
        this.dispather = builder.dispather;
        this.interceptors = builder.interceptors;
        this.retryTimes = builder.retryTimes;
        this.connectionPool = builder.connectionPool;
    }

    /**
     * 生成一个网络请求Call对象实例
     * @param request
     * @return
     */
    public Call newCall(Request request){
        return new Call(this,request);
    }

    public static final class Builder {
        //设置调度器
        private Dispatcher dispather;
        List<Interceptor> interceptors = new ArrayList<>();
        private int retryTimes;
        private ConnectionPool connectionPool;

        public Builder() {
        }

        public static Builder aHttpClient() {
            return new Builder();
        }

        public Builder setDispather(Dispatcher dispather) {
            this.dispather = dispather;
            return this;
        }

        public Builder setInterceptors(List<Interceptor> interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        public Builder setRetryTimes(int retryTimes) {
            this.retryTimes = retryTimes;
            return this;
        }

        public Builder setConnectionPool(ConnectionPool connectionPool) {
            this.connectionPool = connectionPool;
            return this;
        }

        public HttpClient build() {
            if (null == dispather) {
                dispather = new Dispatcher();
            }

            if (null == connectionPool) {
                connectionPool = new ConnectionPool();
            }
            return new HttpClient(this);
        }
    }
}
