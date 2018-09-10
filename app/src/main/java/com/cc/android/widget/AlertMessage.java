package com.cc.android.widget;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.cc.android.R;

/**
 * User:XueBi
 * Date:2018/6/21
 * Time:16:39
 * Description:
 */
public class AlertMessage {
    private Context context;
    private EditText content;
    public AlertMessage(Context context) {
        this.context = context;
    }

    public AlertDialog.Builder dialogShow(){
        final EditText editText = (EditText) LayoutInflater.from(context).inflate(R.layout.alert_message, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog_soft_input).setView(editText);
        return builder;

    }
}


