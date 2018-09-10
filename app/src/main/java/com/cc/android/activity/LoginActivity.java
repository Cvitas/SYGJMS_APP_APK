package com.cc.android.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.cc.android.R;
import com.cc.android.base.BaseActivity;
import com.cc.android.db.DB_Object;
import com.cc.android.entity.RspLogin;
import com.cc.android.entity.RspOk;
import com.cc.android.net.Api;
import com.cc.android.net.NetUtils;
import com.cc.android.tools.SystemStatusManager;
import com.cc.android.utils.StringUtil;
import com.cc.android.utils.Utils;
import com.cc.android.widget.KeyboardLinearLayout;
import com.cc.android.widget.Toast;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_text;
    private KeyboardLinearLayout finalView;
    private ImageView iv_login_bg;
    private ImageView iv_login_next;
    private ProgressBar pb_rotate;
    private LinearLayout ll_err;
    private TextView tv_err_info;
    private EditText et_user;
    private EditText et_pwd;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTranslucentStatus();

        if (!isTaskRoot()) {
            finish();
            return;
        }
        finalView = (KeyboardLinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_login, null);
        finalView.setOnKeyBoardChangeListener(onKeyBoardChangeListener);

        setContentView(finalView);
        btn_text = (Button) findViewById(R.id.btn_login);
        btn_text.setOnClickListener(this);

        iv_login_bg = (ImageView) findViewById(R.id.iv_login_bg);
        iv_login_next = (ImageView) findViewById(R.id.iv_login_next);
        pb_rotate = (ProgressBar) findViewById(R.id.pb_rotate);

        ll_err = (LinearLayout) findViewById(R.id.ll_err);
        tv_err_info = (TextView) findViewById(R.id.tv_err_info);
        et_user = (EditText) findViewById(R.id.et_user);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
            tintManager.setStatusBarTintResource(R.color.head_color);
            getWindow().getDecorView().setFitsSystemWindows(true);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_login:
                pb_rotate.setVisibility(View.VISIBLE);
                iv_login_next.setVisibility(View.INVISIBLE);
                final String userName = et_user.getText().toString();
                String userPwd = et_pwd.getText().toString();
                doLogin(userName, userPwd);
                break;
        }
    }

    private void doLogin(final String userName, final String userPwd) {
//        if(!Utils.isMobileNO(userName)){
//            pb_rotate.setVisibility(View.INVISIBLE);
//            iv_login_next.setVisibility(View.VISIBLE);
//            ll_err.setVisibility(View.VISIBLE);
//            tv_err_info.setText("请输入正确的手机号码！");
//            return;
//        }
        if(StringUtil.isNullOrEmpty(userName)){
            pb_rotate.setVisibility(View.INVISIBLE);
            iv_login_next.setVisibility(View.VISIBLE);
            ll_err.setVisibility(View.VISIBLE);
            tv_err_info.setText("请输入账号！");
            return;
        }
        if(StringUtil.isNullOrEmpty(userPwd)){
            pb_rotate.setVisibility(View.INVISIBLE);
            iv_login_next.setVisibility(View.VISIBLE);
            ll_err.setVisibility(View.VISIBLE);
            tv_err_info.setText("请输入密码！");
            return;
        }
        Api.Login(self, userName, userPwd, new NetUtils.NetCallBack<RspLogin>() {
            @Override
            public void success(RspLogin rspData) {
                Api.setToken(rspData.getUser().getUserId());
                Intent intent = new Intent(self, Location_Activity.class);
                startActivity(intent);
                rightToleft();
            }

            @Override
            public void failed(String msg) {
                System.out.println(msg);
                Toast.show(self,msg);
            }
        });
    }

    /**
     * 软键盘弹出与隐藏监控
     */
    public KeyboardLinearLayout.OnKeyBoardChangeListener onKeyBoardChangeListener = new KeyboardLinearLayout.OnKeyBoardChangeListener() {
        @Override
        public void onKeyBoardStateChange(int state) {
            // TODO Auto-generated method stub
            switch (state) {
                case KeyboardLinearLayout.KEYBOARD_STATE_INIT:
                    break;
                case KeyboardLinearLayout.KEYBOARD_STATE_HIDE:
                    iv_login_bg.setImageResource(R.drawable.bg_login);
                    break;
                case KeyboardLinearLayout.KEYBOARD_STATE_SHOW:
                    iv_login_bg.setImageResource(R.drawable.bg_login_small);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
