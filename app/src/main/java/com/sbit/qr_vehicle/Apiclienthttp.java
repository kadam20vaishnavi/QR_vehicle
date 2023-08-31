package com.sbit.qr_vehicle;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Apiclienthttp {
    private OkHttpClient client;

    public Apiclienthttp() {
        client = new OkHttpClient();
    }

    public void makeApiCall(String url, final ApiResponseCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    callback.onSuccess(responseBody);
                } else {
                    callback.onError(new Exception("API call failed"));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
            }
        });
    }

    public interface ApiResponseCallback {
        void onSuccess(String response);
        void onError(Exception e);
    }
}
