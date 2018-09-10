package com.cc.android.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by yh on 2016/6/15.
 */
public class RspLogin {

    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public class User implements Serializable{
        private String userId;
        public String getUserId() {
            return userId;
        }
        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    public static class RspWrapper extends RspResult<RspLogin>{
    }
}
