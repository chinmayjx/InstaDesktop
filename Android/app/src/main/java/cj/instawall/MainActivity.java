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
    String get_all_links,insert_dwn_btns;
    WebView wv;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        wv = findViewById(R.id.webView);
        Button run = findViewById(R.id.run);

        try {
            Scanner s = new Scanner(getAssets().open("scripts/get_all_links.js")).useDelimiter("\\A");
            get_all_links = s.hasNext() ? s.next() : "";
            s = new Scanner(getAssets().open("scripts/insert_dwn_btns.js")).useDelimiter("\\A");
            insert_dwn_btns = s.hasNext() ? s.next() : "";

        } catch (IOException e) {
            e.printStackTrace();
        }


        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient(){
            @Override

            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                String action = "";
                String mess = consoleMessage.message();
                for (int i=0;i<mess.length();i++){
                    if(mess.charAt(i)==':'){
                        mess = mess.substring(i+1);
                        break;
                    }
                    action+=mess.charAt(i);
                }
                Log.d(TAG, action + '\n' + mess);
                Log.d(TAG, "onConsoleMessage: " + consoleMessage.message() + " -- From line " + consoleMessage.lineNumber() + " of " + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);

        wv.loadUrl("https://www.instagram.com/chinmayjain08/saved");

        run.setOnClickListener(view -> {
            wv.evaluateJavascript(insert_dwn_btns, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {

                }
            });
        });
    }

    @Override
    public void onBackPressed() {
        if(wv!=null&&wv.canGoBack()){
            wv.goBack();
        }
        else{
            super.onBackPressed();
        }
    }
}