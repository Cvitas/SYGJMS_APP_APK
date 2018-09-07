package com.cc.android;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import com.cc.android.db.LoadDataTask;
import com.cc.android.tools.CrashHandler;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Created by yh on 2016/6/13.
 */
public class KeDaoApplication extends Application {
    private static Context mContext;
    private static KeDaoApplication self;
    public static Typeface typeFace;

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;
        mContext = getApplicationContext();

        CrashHandler.getInstance().init(mContext);
        (new LoadDataTask(mContext)).execute();
        setTypeface();
    }

    public void setTypeface(){
        typeFace = Typeface.createFromAsset(getAssets(), "fonts/SourceHanSansCN-Regular.otf");
        try
        {
            Field field = Typeface.class.getDeclaredField("SERIF");
            field.setAccessible(true);
            field.set(null, typeFace);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public static KeDaoApplication getInstance() {
        return self;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
