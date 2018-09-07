package com.cc.android.enums;

/**
 * Created by yh on 2016/6/15.
 */
public enum RequestType {

    SAVE_POSITION("MapPoint/AddUserPoint");//登录

    private String value;

    RequestType(String value) {
        this.value = value;
    }

    public String getConstValue() {
        return this.value;
    }

    public boolean compareValue(String compare) {
        if (compare == null) {
            return false;
        }
        return this.getConstValue().equals(compare);
    }
}
