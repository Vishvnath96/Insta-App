package com.example.vps.ui.detail;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vps.R;


public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    public static final String URL = "url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        this.webView = (WebView) findViewById(R.id.activity_web_view);
       webView.getSettings().setJavaScriptEnabled(true);
       if(getIntent() != null && getIntent().getStringExtra(URL) != null){
           webView.loadUrl(getIntent().getStringExtra(URL));
       }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
              //If there is history, then the canGoBack method will return ?true?//
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
