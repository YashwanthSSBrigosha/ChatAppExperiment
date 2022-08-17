package com.heyletscode.chattutorial;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketUtility {
    private static final String TAG = "WebSocketUtility";
    public static List<JSONObject> messages = new ArrayList<>();
    public static WebSocket webSocket;
    private String SERVER_PATH = "wss://lv7pjsdz9l.execute-api.ap-south-1.amazonaws.com/dev";
    public MessageAdapter messageAdapter = new MessageAdapter(messages);

    void initiateSocketConnection() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());

    }

    private static class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            try {
                JSONObject jsonObject = new JSONObject(text);
                //Log.i(TAG, "onMessage: " + jsonObject.get("isSent"));
                jsonObject.put("isSent", false);
                Log.i(TAG, "onMessage:after put " + jsonObject.get("isSent"));
                messages.add(jsonObject);

                //messageAdapter.addItem(jsonObject);
//
//                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
