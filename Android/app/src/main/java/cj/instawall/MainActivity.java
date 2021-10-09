package cj.instawall;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    final String TAG = "CJ";
    String HWV_FLAG = "", HWV_LAST_VISIT = "", POST_DOWNLOAD_ACTION = "";
    String get_all_links, insert_dwn_btns, download_post;
    int DOWNLOAD_STACK_COUNT = 0;
    String username = "chinmayjain08";
    Random rnd = new Random();
    HashMap<String, HashSet<String>> url_to_name;
    ArrayList<String> recent_downloads = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;
    WebView wv, hwv;
    WebChromeClient wvChromeClient;
    WebViewClient wvClient;
    Button run, hide_wv, sync, random;

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
//        hwv = new WebView(this);

        run = findViewById(R.id.run);
        hide_wv = findViewById(R.id.hide_wv);
        sync = findViewById(R.id.sync);
        random = findViewById(R.id.random);
        readObjects();
        readScripts();
        setupWV();
        setupHWV();

        run.setOnClickListener(view -> {
//            Log.d(TAG, String.valueOf(url_to_name.keySet().size()));
//            wv.evaluateJavascript(download_post,null);
//            print_url_to_name();
//            startService(new Intent(this,MainService.class));

//            registerReceiver(new MainReceiver(),new IntentFilter("cj.instawall.random"));
//            sendBroadcast(new Intent().setAction("cj.instawall.random"));
        });
        run.setOnLongClickListener(view -> {
//            saveObject(url_to_name, "url_to_name_backup");
//            Log.d(TAG, "Backedup");
            startActivity(new Intent(this, DumbActivity.class));
            return true;
        });
        hide_wv.setOnClickListener(view -> {
            if (wv.getVisibility() == View.VISIBLE) wv.setVisibility(View.INVISIBLE);
            else wv.setVisibility(View.VISIBLE);
        });
        hide_wv.setOnLongClickListener(view -> {
            get_linked_file_count();
            return true;
        });
        sync.setOnClickListener(view -> {
            wv.loadUrl("https://www.instagram.com/chinmayjain08/saved");
//            wv.loadUrl("https://www.instagram.com/chinmayjain08");
        });
        sync.setOnLongClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Syncing", Toast.LENGTH_SHORT).show();
            HWV_FLAG = "sync";
            hwv.loadUrl("https://www.instagram.com/chinmayjain08/saved");
            return true;
        });


        random.setOnClickListener(view -> {
            try {
                String[] keys = url_to_name.keySet().toArray(new String[url_to_name.keySet().size()]);
                String purl = keys[rnd.nextInt(url_to_name.keySet().size())];
                if (url_to_name.get(purl).size() > 0) {
                    Log.d(TAG, "post on device");
                    wallpaperFromOffline(purl);
                } else {
                    recent_downloads.clear();
                    HWV_FLAG = "download_post";
                    HWV_LAST_VISIT = purl;
                    POST_DOWNLOAD_ACTION = "wallpaper";
                    hwv.loadUrl("https://instagram.com" + purl);
                    random.setText("");
                    random.setClickable(false);
                }
            } catch (Exception e) {
                Log.d(TAG, Log.getStackTraceString(e));
            }
        });

        random.setOnLongClickListener(view -> {
            String fnm = sharedPreferences.getString("current_wallpaper", null);
            if (fnm != null) {
                sendWallToDesktop(fnm);
                Toast.makeText(getApplicationContext(), "sending", Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        // --------------------

//        setWallpaper("197849760_234294944779150_2759185063273606450_n.jpg");

        // --------------------
    }


    void saveObject(Object o, String n) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(getFilesDir(), n)));
            out.writeObject(o);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    void readObjects() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(getFilesDir(), "url_to_name")));
            url_to_name = (HashMap<String, HashSet<String>>) in.readObject();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "FileNotFound");
            url_to_name = new HashMap<>();
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        }
        if (url_to_name == null || url_to_name.keySet().size() == 0) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(getFilesDir(), "url_to_name_backup")));
                url_to_name = (HashMap<String, HashSet<String>>) in.readObject();
            } catch (Exception e) {
                Log.d(TAG, "errrrrrr");
            }
        }
        Toast.makeText(getApplicationContext(), String.valueOf(url_to_name.keySet().size()), Toast.LENGTH_SHORT).show();

    }

    void readScripts() {
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

    void setupHWV() {
        hwv.getSettings().setJavaScriptEnabled(true);
        hwv.getSettings().setDomStorageEnabled(true);
        hwv.setWebViewClient(wvClient);
        hwv.setWebChromeClient(wvChromeClient);
    }

    void setupWV() {
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wvClient = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (view == wv) {
//                    if(wv.getUrl().equals("http://i"))
                    Log.d(TAG, wv.getUrl());
                    wv.evaluateJavascript(insert_dwn_btns, null);
                }
                if (view == hwv) {
                    if (HWV_FLAG.equals("sync")) {
                        HWV_FLAG = "";
                        hwv.evaluateJavascript(get_all_links, null);
                    } else if (HWV_FLAG.equals("download_post")) {
                        Log.d(TAG, "download_post");
                        HWV_FLAG = "";
                        hwv.evaluateJavascript(download_post, null);
                    }
                }
                super.onPageFinished(view, url);
            }
        };

        wvChromeClient = new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                String action = "";
                String mess = consoleMessage.message();
                for (int i = 0; i < mess.length(); i++) {
                    if (mess.charAt(i) == ':') {
                        mess = mess.substring(i + 1);
                        break;
                    }
                    action += mess.charAt(i);
                }
                if (action.equals("flag_hwv")) {
                    HWV_FLAG = mess;
                }
                if (action.equals("post_link")) {
                    url_to_name.put(mess, new HashSet<>());
                }
                if (action.equals("visit")) {
                    try {
                        if (url_to_name.get(mess).size() > 0) {
                            Log.d(TAG, "offline post");
                            wallpaperFromOffline(mess);
                        } else {
                            HWV_LAST_VISIT = mess;
                            POST_DOWNLOAD_ACTION = "wallpaper";
                            recent_downloads.clear();
                            hwv.loadUrl("http://instagram.com" + mess);
                        }
                    } catch (Exception e) {
                    }
                    ;

                }
                if (action.equals("download_cnt")) {
                    DOWNLOAD_STACK_COUNT = Integer.parseInt(mess);
                }
                if (action.equals("download")) {
                    downloadFile(mess);
                }
                Log.d(TAG, action + " : " + mess);
                if (action.isEmpty())
                    Log.d(TAG, "onConsoleMessage: " + consoleMessage.message() + " -- From line " + consoleMessage.lineNumber() + " of " + consoleMessage.sourceId());
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
        saveObject(url_to_name, "url_to_name");
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (wv != null && wv.canGoBack()) {
            wv.goBack();
        } else {
            super.onBackPressed();
        }
    }

    String filenameFromUrl(String url) {
        Pattern p = Pattern.compile("\\w*.jpg");
        Matcher m = p.matcher(url);
        if (m.find()) {
            return url.substring(m.start(), m.end());
        }
        return "none";
    }

    void sendWallToDesktop(String fnm) {
        new Thread(() -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL("http://192.168.29.229:8080").openConnection();
                conn.setRequestMethod("POST");
                byte[] buffer = new byte[1024];
                OutputStream out = conn.getOutputStream();
                FileInputStream in = new FileInputStream(new File(getExternalFilesDir(null), fnm));
                int rd = 0;
                while ((rd = in.read(buffer)) > 0) {
                    out.write(buffer, 0, rd);
                }
                out.flush();
                out.close();

                Log.d(TAG, "Written");

                rd = conn.getInputStream().read(buffer);
                Log.d(TAG, new String(Arrays.copyOfRange(buffer, 0, rd)));

            } catch (Exception e) {
                Log.d(TAG, Log.getStackTraceString(e));
            }
        }).start();
    }

    void wallpaperFromOffline(String post) {
        try {
            HashSet<String> ts = url_to_name.get(post);
            String[] list = ts.toArray(new String[ts.size()]);
            Log.d(TAG, Arrays.toString(list));
            String rf = list[rnd.nextInt(list.length)];
            while (list.length > 0 && !new File(getExternalFilesDir(null), rf).exists()) {
                url_to_name.get(post).remove(rf);
                ts = url_to_name.get(post);
                list = ts.toArray(new String[ts.size()]);
                rf = list[rnd.nextInt(list.length)];
            }
            Log.d(TAG, rf);
            setWallpaper(rf);
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        }
    }

    void setWallpaper(String fnm) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(getExternalFilesDir(null).getAbsolutePath() + "/" + fnm);
            DisplayMetrics met = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(met);
            int w = met.widthPixels;
            int h = met.heightPixels;
            int sh = (int) ((float) w / (float) bitmap.getWidth() * (float) bitmap.getHeight());
            Bitmap background = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(background);
            bitmap = Bitmap.createScaledBitmap(bitmap, w, sh, true);

            canvas.drawBitmap(bitmap, 0, (h - sh) / 2, new Paint());

            background.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File(getExternalFilesDir(null), "test.png")));
            Log.d(TAG, String.valueOf(sh));
            WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
            manager.setBitmap(background);
            spEditor.putString("current_wallpaper", fnm);
            spEditor.commit();
            Log.d(TAG, "Wallpaper set");
            sendWallToDesktop(fnm);
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        }
        random.post(() -> {
            random.setClickable(true);
            random.setText("W");
            Toast.makeText(getApplicationContext(), "Wallpaper Changed", Toast.LENGTH_SHORT).show();
        });
    }

    void downloadFile(String pu) {
        String[] tmp = pu.split(" ");
        String post_url = tmp[0];
        final String url = tmp[1];
        String filename = filenameFromUrl(url);
        Log.d(TAG, post_url + " | " + filename);
        new Thread(() -> {
            try {
                File f = new File(getExternalFilesDir(null), filename);
                if (f.exists()) {
                    Log.d(TAG, "already exists");
                } else {
                    BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                    FileOutputStream out = new FileOutputStream(f);
                    byte[] buffer = new byte[1024];
                    int rd;
                    while ((rd = in.read(buffer)) > 0) {
                        out.write(buffer, 0, rd);
                    }
                    out.close();
                    in.close();
                }
                Log.d(TAG, "success");
                url_to_name.get(post_url).add(filename);
                recent_downloads.add(filename);

            } catch (Exception e) {
                Log.d(TAG, Log.getStackTraceString(e));
            }
            DOWNLOAD_STACK_COUNT--;
            if (POST_DOWNLOAD_ACTION.equals("wallpaper")) {
                if (DOWNLOAD_STACK_COUNT == 0) {
                    int ri = rnd.nextInt(recent_downloads.size());
                    Log.d(TAG, "Set wallpaper" + recent_downloads.get(ri));
                    setWallpaper(recent_downloads.get(ri));
                }
            }
        }).start();
    }

    void print_url_to_name() {
        try {
            int ct = 0;
            for (String k : url_to_name.keySet()) {
                Log.d(TAG, k);
                ct += url_to_name.get(k).size();
                Log.d(TAG, Arrays.toString(url_to_name.get(k).toArray()));
            }
            ;
            Log.d(TAG, "number of linked files : " + String.valueOf(ct));
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        }
    }

    void get_linked_file_count() {
        HashSet<String> files = new HashSet<>();
        int ct = 0;
        try {
            File dir = new File(getExternalFilesDir(null).getAbsolutePath());
            for (File f : dir.listFiles()) {
                String t = f.getName();
                if (t.endsWith(".jpg")) files.add(t);
            }
            for (String k : url_to_name.keySet()) {
                for (String n : url_to_name.get(k)) {
                    if (new File(getExternalFilesDir(null), n).exists()) {
                        ct++;
                    }
                }
            }
            Toast.makeText(getApplicationContext(), String.valueOf(ct) + "/" + String.valueOf(files.size()), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }
}