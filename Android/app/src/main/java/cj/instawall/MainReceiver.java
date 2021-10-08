package cj.instawall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MainReceiver extends BroadcastReceiver {
    String TAG = "CJ";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "receiver");
        context.startService(new Intent(context,MainService.class));
    }
}
