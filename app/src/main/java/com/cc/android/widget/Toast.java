package com.cc.android.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cc.android.R;

/**
 * Created by yh on 2016/6/22.
 */
public class Toast {
    private static long lastShowTime = 0;

    public static void show(Context context, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        long time = System.currentTimeMillis();
        long timeD = time - lastShowTime;
        if (0 < timeD && timeD < 3000) {
            return;
        }
        lastShowTime = time;
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context) .inflate(R.layout.layout_custom_toast, null);
        LinearLayout ll_toast = (LinearLayout) layout.findViewById(R.id.ll_toast);
        ll_toast.getBackground().setAlpha(150);
        TextView tv_toast = (TextView) layout.findViewById(R.id.tv_toast);
        tv_toast.setText(msg);
        android.widget.Toast toast = new android.widget.Toast(context);
        toast.setView(layout);
        toast.setGravity(Gravity.TOP,0,0);
        toast.setDuration(android.widget.Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void show(Context context, String msg, int position) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        long time = System.currentTimeMillis();
        long timeD = time - lastShowTime;
        if (0 < timeD && timeD < 3000) {
            return;
        }
        lastShowTime = time;
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context) .inflate(R.layout.layout_custom_toast, null);
        LinearLayout ll_toast = (LinearLayout) layout.findViewById(R.id.ll_toast);
        ll_toast.getBackground().setAlpha(150);
        TextView tv_toast = (TextView) layout.findViewById(R.id.tv_toast);
        tv_toast.setText(msg);
        android.widget.Toast toast = new android.widget.Toast(context);
        toast.setView(layout);
        toast.setGravity(position,0,0);
        toast.setDuration(android.widget.Toast.LENGTH_LONG);
        toast.show();
    }
}
