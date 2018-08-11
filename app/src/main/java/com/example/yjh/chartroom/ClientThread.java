package com.example.yjh.chartroom;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ClientThread implements Runnable {
    private Socket socket = null;

    private Handler sendHandler;

    public Handler receiveHandler;

    BufferedReader bufferedReader = null;

    BufferedOutputStream bufferedOutputStream = null;

    public ClientThread(Handler handler) {
        this.sendHandler = handler;
    }


    @Override
    public void run() {
        try {
            socket = new Socket("192.168.43.230", 10000);
            bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String content = null;
                    try {
                        while ((content = bufferedReader.readLine()) != null) {
                            Message message = new Message();
                            message.what = 0x123;
                            message.obj = content;
                            sendHandler.sendMessage(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Looper.prepare();
            receiveHandler = new Handler() {
                @Override
                public void handleMessage(Message message) {
                    if (message.what == 0x345) {
                        try {
                            bufferedOutputStream.write((message.obj.toString() + "\r\n").getBytes("utf-8"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();
        } catch (SocketTimeoutException e) {
            System.out.println("网络连接请求超时!!!");
        } catch (SocketException e) {
            System.out.println("连接服务器失败!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
