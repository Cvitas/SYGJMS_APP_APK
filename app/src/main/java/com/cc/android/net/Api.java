package com.cc.android.net;

import android.content.Context;
import com.cc.android.entity.RspOk;
import com.cc.android.enums.RequestType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yh on 2016/6/15.
 */
public class Api {

    public static Map<String, String> tokenMap = new HashMap<String, String>();

    public static Map<String, String> getToken() {
        System.out.println("token=" + tokenMap.get("token"));
        return tokenMap;
    }

    public static void setToken(String token) {
        tokenMap.put("token", token);
    }

    /**
     * 登录
     *
     * @param context     activity 上下文
     * @param username    用户名
     * @param password    密码
     * @param netCallBack 回调
     */
    public static void Login(Context context, String username, String password, NetUtils.NetCallBack<RspOk> netCallBack) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("password", password);
        params.put("username", username);
        NetUtils.post(context, RequestType.APP_LOGIN.getConstValue(), params, netCallBack, RspOk.RspWrapper.class);
    }

    /**
     * 登录
     *
     * @param context     activity 上下文
     * @param netCallBack 回调
     */
    public static void savePosition(Context context, Map<String, String> params, NetUtils.NetCallBack<RspOk> netCallBack) {
        try {

            NetUtils.post(context, RequestType.SAVE_POSITION.getConstValue(), params, getToken(),netCallBack, RspOk.RspWrapper.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
