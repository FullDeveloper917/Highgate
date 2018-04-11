package com.highgate.highgate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebActivity extends AppCompatActivity {

    private WebView loginWebView;
    private String urlStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = getIntent();
        urlStr = intent.getStringExtra("url");

        Toast.makeText(this, "Just waiting...", Toast.LENGTH_LONG).show();

        loginWebView = (WebView) findViewById(R.id.loginWebView);
        WebSettings webSettings = loginWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        loginWebView.loadUrl(urlStr);
        loginWebView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onBackPressed() {
        if(loginWebView.canGoBack()){
            loginWebView.goBack();
        }
        else{
            super.onBackPressed();
        }
    }
}
