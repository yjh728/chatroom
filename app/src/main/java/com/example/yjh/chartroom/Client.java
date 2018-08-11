package com.example.yjh.chartroom;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class Client extends AppCompatActivity {
    private List<Msg> list = new ArrayList<>();
    private EditText editText;
    private Button button;
    private RecyclerView recyclerView;
    private MsgAdpater msgAdpater;
    private Handler handler;
    private ClientThread clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        setContentView(R.layout.client_layout);
        editText = findViewById(R.id.input_text);
        button = findViewById(R.id.send);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayout);
        msgAdpater = new MsgAdpater(list);
        recyclerView.setAdapter(msgAdpater);
        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    Msg msg1 = new Msg(msg.obj.toString(), Msg.TYPE_RECEIVED);
                    list.add(msg1);
                    msgAdpater.notifyItemInserted(list.size() - 1);
                    recyclerView.scrollToPosition(list.size() - 1);
                }
            }
        };
        clientThread = new ClientThread(handler);
        new Thread(clientThread).start();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = editText.getText().toString();
                if (!s.equals("") && s != null) {
                    Msg msg = new Msg(s, Msg.TYPE_SEND);
                    list.add(msg);
                    Message message = new Message();
                    message.what = 0x345;
                    message.obj = s;
                    clientThread.receiveHandler.sendMessage(message);
                    msgAdpater.notifyItemInserted(list.size() - 1);
                    recyclerView.scrollToPosition(list.size() - 1);
                    editText.setText("");
                }
            }
        });
    }
}
