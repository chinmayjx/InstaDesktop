package cj.instawall;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    final String TAG = "CJ";
    String get_all_links;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        WebView wv = findViewById(R.id.webView);
        Button run = findViewById(R.id.run);

        try {
            Scanner s = new Scanner(getAssets().open("scripts/get_all_links.js")).useDelimiter("\\A");
            get_all_links = s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            e.printStackTrace();
        }


        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

                Log.d(TAG, "onConsoleMessage: " + consoleMessage.message() + " -- From line " + consoleMessage.lineNumber() + " of " + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);

        wv.loadUrl("https://www.instagram.com/chinmayjain08/saved");

        run.setOnClickListener(view -> {
            wv.evaluateJavascript(get_all_links, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    Log.d(TAG, "onReceiveValue: " + s);
                }
            });
        });
    }
}