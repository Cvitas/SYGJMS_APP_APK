package com.cc.android.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import com.cc.android.KeDaoApplication;

import java.io.*;
import java.util.List;

/**
 * Created by yh on 2016/6/13.
 */
public class Config {

    public static int width = 0;
    private static int height = 0;
    private static float density;
    private static int displayheight;

    private static String WAITER_LIST="WAITER_INFO";


    /**
     * @paramcontext
     */
    public static void setScreenSize(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        density = displayMetrics.density;
        displayheight = height - getStatusBarHeight(activity);
    }

    /**
     * @param v
     * @return
     */
    public static float getCaleValue(float v) {
        return (v * width / 640);
    }

    /**
     * @param w
     * @return
     */
    public static int getCaleWidth(int w) {
        return (w * width / 640);
    }

    /**
     * @param h
     * @return
     */
    public static int getCaleHeight(int h) {
        return (h * displayheight / 1136);
    }

    /**
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        //if(isShowNavigationBar(context)){
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");

        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId); //��ȡ�߶�
        }
        //}
        return 0;
    }

    /**
     * @param context
     * @return
     */
    public static boolean isShowNavigationBar(Context context) {
        Resources resources = context.getResources();
        int rid = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getBoolean(rid); //�ʾtrue or false
        }
        return false;
    }

    public static Typeface getBoldTypeface() {
        return Typeface.createFromAsset(KeDaoApplication.getContext().getAssets(),
                "fonts/SourceHanSansCN-Bold.otf");
    }

    public static Typeface getRegularTypeface() {
        return Typeface.createFromAsset(KeDaoApplication.getContext().getAssets(),
                "fonts/SourceHanSansCN-Regular.otf");
    }
}
