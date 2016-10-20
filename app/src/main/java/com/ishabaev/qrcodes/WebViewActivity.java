package com.ishabaev.qrcodes;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    public static final String URL_KEY = "url";

    public static void showActivity(@NonNull Activity activity, @NonNull String url) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(URL_KEY, url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        setContentView(webView);

        webView.setWebViewClient(new WebViewClient() {

        });
        webView.setWebChromeClient(new WebChromeClient() {
        });

        String url = getIntent().getStringExtra(URL_KEY);
        if (url != null) {
            webView.loadUrl(url);
        }
    }
}
