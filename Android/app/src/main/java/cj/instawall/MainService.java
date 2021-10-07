package cj.instawall;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MainService extends Service {

    String TAG = "CJ";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "created");
        WebView wv = new WebView(this);
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                wv.evaluateJavascript("document.querySelector(\".RNmpXc\").click()",null);
                Log.d(TAG, "loaded");
                Log.d(TAG, wv.getUrl());
            }
        });
        wv.loadUrl("https://www.google.com");
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
