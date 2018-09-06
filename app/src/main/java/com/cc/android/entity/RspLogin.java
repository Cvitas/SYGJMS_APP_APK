package com.cc.android.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by yh on 2016/6/15.
 */
public class RspLogin {

    @SerializedName("loginInfo")
    private LoginInfo loginInfo;
    private String loginAccount;
   // private String loginPwd;
    private String token;
    private int isMemberCard;

    private float isSupportAlipayQrcode;
    private float isSupportWechatQrcode;

    private float isOpenOrderQrcode;
    private float isOpenSettleQrcode;

    public float getIsSupportAlipayQrcode() {
        return isSupportAlipayQrcode;
    }

    public void setIsSupportAlipayQrcode(float isSupportAlipayQrcode) {
        this.isSupportAlipayQrcode = isSupportAlipayQrcode;
    }

    public float getIsSupportWechatQrcode() {
        return isSupportWechatQrcode;
    }

    public void setIsSupportWechatQrcode(float isSupportWechatQrcode) {
        this.isSupportWechatQrcode = isSupportWechatQrcode;
    }

    public float getIsOpenOrderQrcode() {
        return isOpenOrderQrcode;
    }

    public void setIsOpenOrderQrcode(float isOpenOrderQrcode) {
        this.isOpenOrderQrcode = isOpenOrderQrcode;
    }

    public float getIsOpenSettleQrcode() {
        return isOpenSettleQrcode;
    }

    public void setIsOpenSettleQrcode(float isOpenSettleQrcode) {
        this.isOpenSettleQrcode = isOpenSettleQrcode;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

//    public String getLoginPwd() {
//        return loginPwd;
//    }
//
//    public void setLoginPwd(String loginPwd) {
//        this.loginPwd = loginPwd;
//    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getIsMemberCard() {
        return isMemberCard;
    }

    public void setIsMemberCard(int isMemberCard) {
        this.isMemberCard = isMemberCard;
    }

    public class LoginInfo implements Serializable{
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
    public class User implements Serializable{
        private long createDatetime;

        private String createUsername;

        private String orgId;

        private int status;

        private String userId;

        private String userName;

        private int userNo;

        private String userPassword;

        private String userPhone;

        private String userRealname;

        public long getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(long createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getCreateUsername() {
            return createUsername;
        }

        public void setCreateUsername(String createUsername) {
            this.createUsername = createUsername;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getUserNo() {
            return userNo;
        }

        public void setUserNo(int userNo) {
            this.userNo = userNo;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getUserRealname() {
            return userRealname;
        }

        public void setUserRealname(String userRealname) {
            this.userRealname = userRealname;
        }
    }

    public static class RspWrapper extends RspResult<RspLogin>{
    }
}
