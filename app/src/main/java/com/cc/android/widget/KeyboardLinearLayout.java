package com.cc.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by yh on 2016/6/17.
 */
public class KeyboardLinearLayout extends LinearLayout {
    public static final byte KEYBOARD_STATE_SHOW = -3;
    public static final byte KEYBOARD_STATE_HIDE = -2;
    public static final byte KEYBOARD_STATE_INIT = -1;

    private OnKeyBoardChangeListener onKeyBoardChangeListener;

    private boolean mHasInit;
    private boolean mHasKeybord;
    private int mHeight;

    public interface OnKeyBoardChangeListener{
        public void onKeyBoardStateChange(int state);
    }

    public KeyboardLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardLinearLayout(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(!mHasInit){
            mHasInit = true;
            mHeight = b;
            if(null != onKeyBoardChangeListener){
                onKeyBoardChangeListener.onKeyBoardStateChange(KEYBOARD_STATE_INIT);
            }
        }else{
            mHeight = mHeight < b ? b : mHeight;
        }

        if(mHasInit && mHeight > b){
            mHasKeybord = true;
            if(null != onKeyBoardChangeListener){
                onKeyBoardChangeListener.onKeyBoardStateChange(KEYBOARD_STATE_SHOW);
            }
            System.out.println("show keyboard...");
        }

        if(mHasInit && mHasKeybord && mHeight == b){
            mHasKeybord = false;
            if(null != onKeyBoardChangeListener){
                onKeyBoardChangeListener.onKeyBoardStateChange(KEYBOARD_STATE_HIDE);
            }
            System.out.println("hide keyboard...");
        }

    }

    public void setOnKeyBoardChangeListener(OnKeyBoardChangeListener onKeyBoardChangeListener) {
        this.onKeyBoardChangeListener = onKeyBoardChangeListener;
    }
}
