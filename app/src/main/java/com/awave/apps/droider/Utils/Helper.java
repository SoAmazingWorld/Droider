package com.awave.apps.droider.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by awave on 2016-01-23.
 */
public class Helper {

    public static final String HOME_URL = "http://droider.ru/page/";
    public static final String NEWS_URL = "http://droider.ru/category/news/page/";
    public static final String APPS_URL = "http://droider.ru/category/apps_and_games/programs/page/";
    public static final String GAMES_URL = "http://droider.ru/category/apps_and_games/games/page/";
    public static final String VIDEOS_URL = "http://droider.ru/category/video/page/";

    public static final String EXTRA_ARTICLE_TITLE = "com.awave.apps.droider.Elements.EXTRA_ARTICLE_TITLE";
    public static final String EXTRA_ARTICLE_URL = "com.awave.apps.droider.Elements.EXTRA_ARTICLE_URL";
    public static final String EXTRA_SHORT_DESCRIPTION = "com.awave.droider.Elements.EXTRA_SHORT_DESCRIPTION";
    private static final String TAG = "Helper";

    public static String youtubeVideo;

    public static CharSequence trimWhiteSpace(CharSequence src){
        if (src == null){
            return "";
        }

        int i = src.length();
        try {
            while (i-- >= 0 && Character.isWhitespace(src.charAt(i))){

            }
        } catch (StringIndexOutOfBoundsException e) {
            Log.d(TAG, "trimWhiteSpace: not needed to trim");
        }

        return src.subSequence(0, i+1);
    }

    public static String getYoutubeImg(String src){
        String img = "";
        if(!src.equals("")) {
            img = "http://img.youtube.com/vi/" + src.substring(src.indexOf("embed/") + 6) + "/0.jpg";
            return img;
        }
        return img;
    }

    public static String trimYoutubeId(String src){
        return src.substring(30);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivity.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void initInternetConnectionDialog(final Context context) {
        new AlertDialog.Builder(context).setTitle("Соединение прервано").setMessage("Проверьте своё соединение с интернетом")
                .setNeutralButton("Включить Wi-Fi?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Helper.enableWiFi(context, true);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create().show();
    }

    public static void enableWiFi(Context c, boolean wifi){
        WifiManager wifiConfiguration = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        wifiConfiguration.setWifiEnabled(wifi);
    }


    public static Typeface getRobotoFont(String type, boolean isItalic, Activity a){
        if (!isItalic){
            switch (type){
                case "Thin":
                    return Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-Thin.ttf");
                case "Light":
                    return Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-Light.ttf");
                case "Medium":
                    return Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-Medium.ttf");
                case "Regular":
                    return Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-Regular.ttf");
                case "Bold":
                    return Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-Bold.ttf");
                default:
                    return Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-Regular");
            }
        }
        else {
            switch (type){
                case "Thin":
                    return Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-ThinItalic.ttf");
                case "Light":
                    return Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-LightItalic.ttf");
                case "Medium":
                    return Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-MediumItalic.ttf");
                case "Bold":
                    return Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-BoldItalic.ttf");
                default:
                    return Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-MediumItalic.ttf");
            }
        }
    }

    public static Bitmap drawableToBitmap(Drawable d){
        Bitmap b;
        if (d instanceof BitmapDrawable){
            BitmapDrawable bitmapDrawable = (BitmapDrawable) d;
            if (bitmapDrawable.getBitmap() != null){
                return bitmapDrawable.getBitmap();
            }
        }

        if (d.getIntrinsicWidth() <= 0 || d.getIntrinsicHeight() <= 0){
            b = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        }
        else {
            b = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(b);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        return b;
    }

    public static void setTransparent(boolean isTransparent, Activity a){
        if (isTransparent && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            a.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            a.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        else {
            a.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            a.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}