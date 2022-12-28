package com.example.client4server;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextViewReplyFromServer;
    private EditText mEditTextSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonSend = (Button) findViewById(R.id.btn_send);
        mEditTextSendMessage = (EditText) findViewById(R.id.edt_send_message);
        mTextViewReplyFromServer = (TextView) findViewById(R.id.tv_reply_from_server);
        buttonSend.setOnClickListener(this);


    } // onCreate

    // @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_send:
                sendMessage(mEditTextSendMessage.getText().toString());
                break;
        }
    }

    private void sendMessage(final String msg) {

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    // Here you need to specify the IP address of the server and the port number for connection.
                    // This data can be received from the server via SMS https://github.com/vallshmeleff/androidsms
                    // If the Client and Server are in different networks (for example, in different cities), the server's IP must be routable.
                    // Such an IP address can be rented from an Internet provider and from a Cellular Operator
                    Socket s = new Socket("xxx.xxx.xxx.xxx", 9012); // IP-address and Port
                    OutputStream out = s.getOutputStream();
                    PrintWriter output = new PrintWriter(out);

                    output.println(msg);
                    output.flush();
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    final String st = input.readLine();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            String s = mTextViewReplyFromServer.getText().toString();
                            if (st.trim().length() != 0)
                                mTextViewReplyFromServer.setText(s + "\nFrom Server : " + st);
                        }
                    });

                    output.close();
                    out.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

// Such a Client can connect to the Server (for example) on the PHP of your webserver.
// Then we will write such a server and add RSA encryption to them.


} // MainActivity