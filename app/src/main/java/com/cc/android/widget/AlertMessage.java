package com.cc.android.widget;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.cc.android.R;

/**
 * User:XueBi
 * Date:2018/6/21
 * Time:16:39
 * Description:
 */
public class AlertMessage {
    public static void dialogShow(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.alert_message, null);
        TextView content = (TextView) v.findViewById(R.id.dialog_content);
        TextView dialog_title = (TextView) v.findViewById(R.id.dialog_title);
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        dialog_title.setText(title);
        content.setText(message);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}


