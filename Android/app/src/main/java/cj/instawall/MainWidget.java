package cj.instawall;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainWidget extends AppWidgetProvider {
    String TAG = "CJ";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i=0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, MainService.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent,0);
            PendingIntent pendingIntent = PendingIntent.getService(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Get the layout for the widget and attach an on-click listener
            // to the button.
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setOnClickPendingIntent(R.id.wid_btn, pendingIntent);
            Log.d(TAG, "click");

            // Tell the AppWidgetManager to perform an update on the current app widget.
            appWidgetManager.updateAppWidget(appWidgetId, views);
            context.startService(intent);
        }


    }
}
