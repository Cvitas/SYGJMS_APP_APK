package com.cc.android.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.cc.android.R;
import com.cc.android.tools.Config;

/**
 * Created by yh on 2016/6/15.
 */
public class CProgressDialog extends Dialog {

    private Context mContext = null;
    private String message;

    public CProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public CProgressDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
    }

    public CProgressDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progressbar);
        TextView msg = (TextView) findViewById(R.id.msg);
        if (message != null)
            msg.setText(message);

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();

        int screenwith = Config.width;
        lp.width = (int)(screenwith*0.85f); // ���
        dialogWindow.setAttributes(lp);



    }

    public CProgressDialog setMessage(String txt) {
        message = txt;
        return this;
    }
}
