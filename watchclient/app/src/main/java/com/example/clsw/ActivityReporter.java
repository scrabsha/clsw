package com.example.clsw;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ActivityReporter {
    private String baseUrl = "http://domino.zdimension.fr:3030";
    private String sessionId;

    public ActivityReporter(String sessionId) {
        this.sessionId = sessionId;
    }

    void reportToBackend() {
        // Send a put request to the backend at the scrabsha/activity/:sessionid
        // endpoint.

        OkHttpClient client = new OkHttpClient();

        String url = baseUrl + "/scrabsha/" + sessionId;

        Log.d(TAG, "reportToBackend: " + url);

        // Create an empty request body.
        RequestBody body = RequestBody.create(null, new byte[0]);

        Request request = new Request.Builder()
                .url(url)
                .post(body).build();

        // Send the request.
        try {
            client.newCall(request).execute().close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Oh no");
        }
    }
}
