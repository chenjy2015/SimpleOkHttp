package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.http.CacheInterceptor;
import com.example.myapplication.http.Call;
import com.example.myapplication.http.CallServiceInterceptor;
import com.example.myapplication.http.Callback;
import com.example.myapplication.http.HeadInterceptor;
import com.example.myapplication.http.HttpClient;
import com.example.myapplication.http.Interceptor;
import com.example.myapplication.http.InterceptorChain;
import com.example.myapplication.http.Request;
import com.example.myapplication.http.RequestBody;
import com.example.myapplication.http.Response;
import com.example.myapplication.http.RetryInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button httpBtn;
    HttpClient client;
    public static final String TAG = "Interceptor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        httpBtn = findViewById(R.id.http_btn);
        httpBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       get();
                    }
                }
        );
        client = new HttpClient.Builder()
                .setRetryTimes(3)
                .build();
    }


    public void get() {
        Request request = new Request.Builder()
                .withHttpUrl("http://www.kuaidi100.com/query?type=yuantong&postid=222222222")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.e("响应体", response.getBody());
            }
        });
    }

    public void post() {
        RequestBody body = new RequestBody()
                .add("key", "064a7778b8389441e30f91b8a60c9b23")
                .add("city", "深圳");
        Request request = new Request.Builder()
                .withHttpUrl("http://restapi.amap.com/v3/weather/weatherInfo")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.e("响应体", response.getBody());
            }
        });
    }
}
