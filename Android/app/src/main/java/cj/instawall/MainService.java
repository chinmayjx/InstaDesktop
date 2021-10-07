package cj.instawall;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MainService extends Service {

    String TAG = "CJ";
    WebManager wm;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "created");
        wm = new WebManager(this);
        wm.layout(0, 0, 5000, 5000);
        Handler han = new Handler();
        Runnable rnb = new Runnable() {
            @Override
            public void run() {
                wm.setRandomWallpaper();
                han.postDelayed(this,10*60*1000);
            }
        };
        han.post(rnb);
        wm.saveObjects();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, String.valueOf(wm==null));
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "save objects");
        wm.saveObjects();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
