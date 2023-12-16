package com.example.natural.model;

public class URL {

    private String mainURL= "https://uiot.ixxc.dev";
    private String urlFormSignUp= "https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/registrations?client_id=openremote&redirect_uri=https%3A%2F%2Fuiot.ixxc.dev%2Fmanager%2F&response_mode=fragment&response_type=code&scope=openid";

    private String urlFormReset = "https://uiot.ixxc.dev/auth/realms/master/login-actions/reset-credentials?client_id=openremote&tab_id=XEi90UopIE0";

    public String getUrlFormReset() {
        return urlFormReset;
    }

    public String GetURLMain(){
        return mainURL;
    }
    public String GetURLFormSignUp(){
        return urlFormSignUp;
    }
}