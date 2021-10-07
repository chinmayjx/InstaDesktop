package cj.instawall;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class DumbActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        setContentView(ll);
        WebManager wm = new WebManager(this);
        wm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ll.addView(wm);
        wm.setRandomWallpaper();
    }
}
