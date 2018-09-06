package com.cc.android.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 服务响应实体
 * Created by yh on 2016/6/15.
 */
public abstract class RspBase implements Serializable{
    // 200 : ok
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
