package com.cc.android.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ilisin on 16/6/30.
 */
public class RspResult<T> extends RspBase{
    @SerializedName("result")
    T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
