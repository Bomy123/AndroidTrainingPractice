package com.zmb.androidtrainingpractice.webviewpractice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.zmb.androidtrainingpractice.R;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {
    public static String URL_TAG = "url";
    WebView webView = null;
    EditText murl = null;
    Button mgo, viewsource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        murl = (EditText) findViewById(R.id.url);
        mgo = (Button) findViewById(R.id.go);
        viewsource = (Button) findViewById(R.id.viewsource);
        webView = (WebView) findViewById(R.id.viewwebpage);
        webView.setWebViewClient(new WebViewClient());
        mgo.setOnClickListener(this);
        viewsource.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go:
                loadUrl();
                break;
            case R.id.viewsource:
                goSourceViewActivity();
                break;
            default:
                loadUrl();
                break;
        }
    }

    private void loadUrl() {
        webView.loadUrl(murl.getText().toString());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(murl.getText().toString());
                return true;
            }
        });
    }

    private void goSourceViewActivity()
    {
        Intent intent = new Intent(WebViewActivity.this,ViewSourceActivity.class);
        intent.putExtra(URL_TAG,murl.getText().toString());
        startActivity(intent);
    }

}
