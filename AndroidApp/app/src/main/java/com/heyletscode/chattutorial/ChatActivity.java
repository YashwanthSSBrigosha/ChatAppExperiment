package com.heyletscode.chattutorial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ChatActivity extends AppCompatActivity implements TextWatcher {

    private static final String TAG = "ChatActivity";
    private String name;
    private EditText messageEdit;
    private View sendBtn, pickImgBtn;
    private RecyclerView recyclerView;
    private int IMAGE_REQUEST_ID = 1;
    private WebSocketUtility webSocketUtility = new WebSocketUtility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        name = getIntent().getStringExtra("name");
        initializeView();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String string = s.toString().trim();

        if (string.isEmpty()) {
            resetMessageEdit();
        } else {

            sendBtn.setVisibility(View.VISIBLE);
            pickImgBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void resetMessageEdit() {

        messageEdit.removeTextChangedListener(this);

        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        pickImgBtn.setVisibility(View.VISIBLE);

        messageEdit.addTextChangedListener(this);

    }

    private void initializeView() {

        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);
        pickImgBtn = findViewById(R.id.pickImgBtn);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(webSocketUtility.messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        messageEdit.addTextChangedListener(this);

        sendBtn.setOnClickListener(v -> {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", name);
                jsonObject.put("message", messageEdit.getText().toString());

                webSocketUtility.webSocket.send(jsonObject.toString());

                jsonObject.put("isSent", true);
                webSocketUtility.messages.add(jsonObject);
                webSocketUtility.messageAdapter.notifyDataSetChanged();

                //recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                resetMessageEdit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        pickImgBtn.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            startActivityForResult(Intent.createChooser(intent, "Pick image"),
                    IMAGE_REQUEST_ID);

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {

            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(is);

                //sendImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

//    private void sendImage(Bitmap image) {
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
//
//        String base64String = Base64.encodeToString(outputStream.toByteArray(),
//                Base64.DEFAULT);
//
//        JSONObject jsonObject = new JSONObject();
//
//        try {
//            jsonObject.put("name", name);
//            jsonObject.put("image", base64String);
//
//            webSocket.send(jsonObject.toString());
//
//            jsonObject.put("isSent", true);
//
//            messageAdapter.addItem(jsonObject);
//
//            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }


}
