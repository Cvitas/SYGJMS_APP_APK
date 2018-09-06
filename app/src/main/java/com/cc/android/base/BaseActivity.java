package com.cc.android.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import com.cc.android.activity.LoginActivity;
import com.cc.android.R;
import com.cc.android.tools.ActivityManager;
import com.cc.android.tools.SystemStatusManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.util.List;

/**
 * Created by yh on 2016/6/13.
 */
public class BaseActivity extends FragmentActivity {
    protected ActivityManager activityManager;

    protected BaseActivity self;
    protected int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        self = this;
        activityManager = ActivityManager.getScreenManager();
        activityManager.pushActivity(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

        PushAgent.getInstance(self).onAppStart();
        if (!(self instanceof LoginActivity)) {
            setTranslucentStatus();
        }
    }

    /**
     * 自定义标题栏
     */
    private void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemStatusManager tintManager = new SystemStatusManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.main_color);
            getWindow().getDecorView().setFitsSystemWindows(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(self);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(self);
    }

    @Override
    protected void onDestroy() {
        activityManager.popActivity(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            this.finish();
            leftToright();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void leftToright() {
        overridePendingTransition(R.anim.ani_left_get_into, R.anim.ani_right_sign_out);
    }

    public void rightToleft() {
        overridePendingTransition(R.anim.ani_right_get_into, R.anim.ani_left_sign_out);
    }

    public String getJsonStringByList(List list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    public List getListByJsonString(String str) {
        Gson gson = new Gson();
        List ps = gson.fromJson(str, new TypeToken<List>() {
        }.getType());
        return ps;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void logActivityName(Activity activity) {
        Log.d("activity name", "this activity name = " + this.getClass().getName());
    }
}
