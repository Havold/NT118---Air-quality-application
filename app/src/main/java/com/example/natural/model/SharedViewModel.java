package com.example.natural.model;
import androidx.lifecycle.ViewModel;
public class SharedViewModel extends ViewModel {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
