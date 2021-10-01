package cj.instawall;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    final String TAG = "CJ";
    String get_all_links,insert_dwn_btns,download_post;
    String username = "chinmayjain08";
    WebView wv,hwv;
    WebChromeClient wvChromeClient;
    WebViewClient wvClient;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        wv = findViewById(R.id.webView);
        hwv = findViewById(R.id.hiddenWebView);
//        hwv = new WebView(getApplicationContext());
        Button run = findViewById(R.id.run);

        readScripts();
        setupWV();
        setupHWV();


        wv.loadUrl("https://www.instagram.com/chinmayjain08/saved");
        run.setOnClickListener(view -> {
            wv.evaluateJavascript(download_post,null);
        });
    }

    void readScripts(){
        try {
            Scanner s = new Scanner(getAssets().open("scripts/get_all_links.js")).useDelimiter("\\A");
            get_all_links = s.hasNext() ? s.next() : "";
            s = new Scanner(getAssets().open("scripts/insert_dwn_btns.js")).useDelimiter("\\A");
            insert_dwn_btns = s.hasNext() ? s.next() : "";
            s = new Scanner(getAssets().open("scripts/download_post.js")).useDelimiter("\\A");
            download_post = s.hasNext() ? s.next() : "";

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setupHWV(){
        hwv.getSettings().setJavaScriptEnabled(true);
        hwv.getSettings().setDomStorageEnabled(true);
        hwv.setWebViewClient(wvClient);
        hwv.setWebChromeClient(wvChromeClient);
    }

    void setupWV(){
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wvClient = new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                if(view == wv) {
                    wv.evaluateJavascript(insert_dwn_btns, null);
                }
                if(view == hwv){
                    hwv.evaluateJavascript(download_post,null);
                }
                super.onPageFinished(view, url);
            }
        };

        wvChromeClient = new WebChromeClient(){
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
                if(action.equals("visit")){
                    hwv.loadUrl("http://instagram.com"+mess);
                }
                if(action.equals("download")){
                    String fnm = "a" + String.valueOf(Math.abs(new Random().nextLong())) + ".jpg";
                    downloadFile(mess,fnm);
                }
                Log.d(TAG, action + " : " + mess);
                if(action.isEmpty())Log.d(TAG, "onConsoleMessage: " + consoleMessage.message() + " -- From line " + consoleMessage.lineNumber() + " of " + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
        };
        wv.setWebViewClient(wvClient);
        wv.setWebChromeClient(wvChromeClient);
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

    void downloadFile(String url, String filename){
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    File f = new File(getExternalFilesDir(null),filename);
                    BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                    FileOutputStream out = new FileOutputStream(f);
                    byte buffer[] = new byte[1024];
                    int rd;
                    while((rd=in.read(buffer))>0){
                        out.write(buffer,0,rd);
                    }
                    out.close();
                    in.close();
                    Log.d(TAG, "success");
                }
                catch (Exception e){
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            }
        }).start();
    }
}