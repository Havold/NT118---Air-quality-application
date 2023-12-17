package com.example.natural.model;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
public class SharedViewModel extends ViewModel {

    private String accessToken;
    String attribute,timeFrame;
    int dayStart,monthStart,yearStart,dayEnd,monthEnd,yearEnd;
    boolean onNotification;

    public boolean isOnNotification() {
        return onNotification;
    }

    public void setOnNotification(boolean onNotification) {
        this.onNotification = onNotification;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public int getDayStart() {
        return dayStart;
    }

    public void setDayStart(int dayStart) {
        this.dayStart = dayStart;
    }

    public int getMonthStart() {
        return monthStart;
    }

    public void setMonthStart(int monthStart) {
        this.monthStart = monthStart;
    }

    public int getYearStart() {
        return yearStart;
    }

    public void setYearStart(int yearStart) {
        this.yearStart = yearStart;
    }

    public int getDayEnd() {
        return dayEnd;
    }

    public void setDayEnd(int dayEnd) {
        this.dayEnd = dayEnd;
    }

    public int getMonthEnd() {
        return monthEnd;
    }

    public void setMonthEnd(int monthEnd) {
        this.monthEnd = monthEnd;
    }

    public int getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(int yearEnd) {
        this.yearEnd = yearEnd;
    }
}
