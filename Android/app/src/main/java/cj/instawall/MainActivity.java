package cj.instawall;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    final String TAG = "CJ";
    String HWV_FLAG;
    String get_all_links,insert_dwn_btns,download_post;
    String username = "chinmayjain08";
    HashMap<String,String> url_to_name;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;
    WebView wv,hwv;
    WebChromeClient wvChromeClient;
    WebViewClient wvClient;
    Button run,hide_wv,sync;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        sharedPreferences = getPreferences(MODE_PRIVATE);
        spEditor = sharedPreferences.edit();
        wv = findViewById(R.id.webView);
        hwv = findViewById(R.id.hiddenWebView);
//        hwv = new WebView(getApplicationContext());
        hwv.setMinimumHeight(1920);
        hwv.setMinimumWidth(1080);
        run = findViewById(R.id.run);
        hide_wv = findViewById(R.id.hide_wv);
        sync = findViewById(R.id.sync);
        readObjects();
        readScripts();
        setupWV();
        setupHWV();

        wv.loadUrl("https://www.instagram.com/chinmayjain08/saved");
        
        run.setOnClickListener(view -> {
            Log.d(TAG, String.valueOf(url_to_name.keySet().size()));
        });
        run.setOnLongClickListener(view -> {

            return true;
        });
        hide_wv.setOnClickListener(view -> {
            if(wv.getVisibility() == View.VISIBLE) wv.setVisibility(View.INVISIBLE);
            else wv.setVisibility(View.VISIBLE);
        });
        sync.setOnClickListener(view -> {
            HWV_FLAG = "sync";
            hwv.loadUrl("https://www.instagram.com/chinmayjain08/saved");
        });
    }

    void saveObject(Object o, String n){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(getFilesDir(),n)));
            out.writeObject(o);
        }
        catch (Exception e){

        }
    }

    void readObjects(){
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(getFilesDir(),"url_to_name")));
            url_to_name = (HashMap<String, String>) in.readObject();
        }
        catch (FileNotFoundException e){
            url_to_name = new HashMap<>();
        }
        catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        }
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
                    if(HWV_FLAG.equals("sync")){
                        hwv.evaluateJavascript(get_all_links,null);
                        HWV_FLAG = "";
                    }
                    else hwv.evaluateJavascript(download_post,null);
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
                if(action.equals("post_link")){
                    url_to_name.put(mess,"");
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
    protected void onDestroy() {
//        Toast.makeText(this, "destroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        saveObject(url_to_name,"url_to_name");
        super.onPause();
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
                    byte[] buffer = new byte[1024];
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