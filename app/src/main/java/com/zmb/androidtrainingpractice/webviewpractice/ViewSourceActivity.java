package com.zmb.androidtrainingpractice.webviewpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.zmb.androidtrainingpractice.R;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ViewSourceActivity extends AppCompatActivity {

    String murl = null;
    TextView sourceview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_source);
        murl = getIntent().getStringExtra(WebViewActivity.URL_TAG);
        sourceview = (TextView) findViewById(R.id.source);
        showSource();
    }
    private void showSource(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getsource();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getsource() throws IOException {
        URL url = new URL(murl);
        URLConnection connection = url.openConnection();
        DataInputStream inputStream = new DataInputStream(connection.getInputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "gb2312"));
        String tmpstr, string = "";

        while ((tmpstr = in.readLine()) != null) {
            string += tmpstr;
        }
        final String finalString = string;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sourceview.setText(finalString);
            }
        });
    }

}
