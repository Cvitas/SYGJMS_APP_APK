package com.cc.android.tools;

import android.app.Activity;
import com.cc.android.activity.LoginActivity;

import java.util.Stack;

/**
 * Created by yh on 2016/6/13.
 */
public class ActivityManager {
    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {
        activityStack = new Stack<Activity>();
    }

    public static ActivityManager getScreenManager() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public void popActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    public Activity currentActivity() {
        Activity activity = null;
        if (!activityStack.empty())
            activity = activityStack.lastElement();
        return activity;
    }

    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public void popAllActivity() {
        while (!activityStack.empty()) {
            Activity activity = currentActivity();
            if (activity != null) {
                popActivity(activity);
            }
        }
        if (activityStack != null) {
            activityStack.clear();
        }
    }

    public void popTopActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = currentActivity();
            if (activity != null && !(activity instanceof LoginActivity)) {
                popActivity(activity);
            }
        }
    }
}
