package com.example.natural.service;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.natural.model.DateUtils;
import com.example.natural.FragmentHome;
import com.example.natural.R;
import com.example.natural.SQLite.DatabaseHelper;
import com.example.natural.api.apiService_token;
import com.example.natural.model.SharedViewModel;
import com.example.natural.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyJobService extends JobService {
    SharedViewModel sharedViewModel;

    apiService_token apiServiceToken;
    boolean stateWeather,stateNight;
    String accessToken,assetID;
    public static final String TAG = MyJobService.class.getName();
    private boolean jobCancelled;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            // Lấy dữ liệu từ Intent
            stateNight = intent.getBooleanExtra("stateNight", false);
            stateWeather = intent.getBooleanExtra("stateWeather", false);
            accessToken = intent.getStringExtra("accessToken");
            assetID = intent.getStringExtra("assetID");
        }

        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {

        Log.e(TAG,"Job started");
        checkAndShowApiReminder(accessToken);
        doBackgroundWork(params);
        return true;
    }



    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (jobCancelled) {
                    return;
                }
                checkAndShowApiReminder(accessToken);
                showReminderNotification();
                Log.e(TAG,"run notification");

                jobFinished(params,false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e(TAG, "Job stopped");
        jobCancelled = true;
        return true;
    }

    private void showReminderNotification() {
        String reminderTxt = "none";
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        if (stateNight==true) {     //Trời tối
            if (stateWeather == false) {  //Nếu như trời mưa
                reminderTxt = getString(R.string.hey_it_s_going_to_rain_today_remember_to_bring_an_umbrella_when_going_out_and_be_careful_of_slippery_roads);
            } else {    // trời trăng
                reminderTxt = getString(R.string.hey_there_will_be_moon_and_stars_tonight_let_s_enjoy_the_night_sky);
            }
        }
        else  {     //Trời sáng
            if (stateWeather == false) {  //Nếu như trời mưa
                reminderTxt = getString(R.string.hey_it_s_going_to_rain_today_remember_to_bring_an_umbrella_when_going_out_and_be_careful_of_slippery_roads);
            } else {    //trời nắng
                reminderTxt = getString(R.string.hey_it_s_going_to_be_sunny_today_let_s_go_out_and_enjoy_it);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), chanelID);
        builder.setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle("Reminder Weather")
                .setContentText(reminderTxt)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(getApplicationContext(), FragmentHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel(chanelID);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID,
                        "Some description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        notificationManager.notify(0,builder.build());
    }

    private void checkAndShowApiReminder(String accessToken) {

        apiServiceToken.api_token
                .getAsset(assetID, "Bearer " + accessToken)
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        WeatherResponse weatherResponse = response.body();
                        if (weatherResponse != null) {
                            float rainfall, humidity, temp, wind;
                            long timestamp;
                            rainfall = weatherResponse.getAttributes().getRainfall().getValue();
                            humidity = weatherResponse.getAttributes().getHumidity().getValue();
                            temp = weatherResponse.getAttributes().getTemperature().getValue();
                            wind = weatherResponse.getAttributes().getWindSpeed().getValue();
                            timestamp = weatherResponse.getCreatedOn();

                            int hour = DateUtils.convertTimestampToHourInt(timestamp);
                            if (hour>=18 || hour<=4) {
                                stateNight=true;
                            }
                            else {
                                stateNight=false;
                            }

                            if (humidity >= 80 || rainfall >= 16 || wind >= 7.2) {
                                stateWeather = false; //Trời mưa
                            } else {
                                stateWeather = true; //Trời nắng, trăng
                            }

                            //        Khai báo để sử dụng SQLite
                            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
//                            dbHelper.deleteTable();
                            dbHelper.insertWeatherData(temp, humidity, wind, rainfall, timestamp);
                            dbHelper.close();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    }
                });
    }
}
