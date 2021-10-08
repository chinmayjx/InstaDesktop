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
        Toast.makeText(getApplicationContext(), "updating...", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "created");
        wm = new WebManager(this);
        wm.layout(0, 0, 5000, 5000);
        wm.setRandomWallpaper();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "destroyed service");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
