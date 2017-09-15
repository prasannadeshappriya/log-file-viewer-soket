package com.example.prasanna_d.socketserver;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private final String url = "http://192.168.101.175:2000";
    private ListView lstMessage;
    private Button btnConnect;
    private EditText etUrl;

    private Socket mSocket;{
        try {
            mSocket = IO.socket(url);
        } catch (URISyntaxException e) {
            Log.i("tmp", e.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSocket.on("chat message", onNewMessage);
        mSocket.connect();
        lstMessage = (ListView)findViewById(R.id.lstMessage);
        btnConnect = (Button)findViewById(R.id.btnConnect);
        etUrl = (EditText)findViewById(R.id.etUrl);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!etUrl.getText().toString().equals("")) {
                        try {
                            mSocket = IO.socket(etUrl.getText().toString());
                        } catch (URISyntaxException e) {
                            Log.i("tmp", e.toString());
                        }
                        mSocket.on("chat message", onNewMessage);
                        mSocket.connect();
                    }
                }catch (Exception err){
                    Log.i("tmp",err.toString());
                }
            }
        });
    }

    public Activity getActivity(){return this;}

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<String> lstItems = Arrays.asList(args[0].toString().split("\r\n"));
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,lstItems);
                    lstMessage.setAdapter(adapter);
                }
            });
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("chat message", onNewMessage);
    }
}
