package com.example.natural.service;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.natural.FragmentHome;
import com.example.natural.R;
import com.example.natural.SQLite.DatabaseHelper;
import com.example.natural.model.WeatherResponse;
import com.example.natural.api.apiService_token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyService extends Service {
    String accessToken,assetID;
    apiService_token apiServiceToken;
    boolean stateWeather;
    private static final long REMINDER_INTERVAL = 5 * 60 * 1000; // 5 minutes
    private static final long API_CALL_INTERVAL = 30 * 60 * 1000; // 30 minutes

    private Handler handler = new Handler();
    private Runnable reminderRunnable;
    private Runnable apiCallRunnable;

    @Override
    public void onCreate() {
        super.onCreate();

        // Khởi tạo Runnable cho việc gửi Reminder
        reminderRunnable = new Runnable() {
            @Override
            public void run() {
                // Gửi thông báo Reminder
                showReminderNotification();

                // Lập lịch cho việc chạy lại Reminder sau 5 phút
                handler.postDelayed(this, REMINDER_INTERVAL);
            }
        };

        // Khởi tạo Runnable cho việc gọi API
        apiCallRunnable = new Runnable() {
            @Override
            public void run() {
                // Gọi API và kiểm tra trạng thái (Sunny hoặc Rainy) để gửi Reminder nếu cần
                checkAndShowApiReminder(accessToken);

                // Lập lịch cho việc chạy lại Runnable sau 30 phút
                handler.postDelayed(this, API_CALL_INTERVAL);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null) {
            // Lấy dữ liệu từ Intent
            stateWeather = intent.getBooleanExtra("stateWeather",false);
            accessToken = intent.getStringExtra("accessToken");
            assetID = intent.getStringExtra("assetID");

            // Bắt đầu chạy Runnable cho Reminder
            handler.postDelayed(reminderRunnable, REMINDER_INTERVAL);

            // Bắt đầu chạy Runnable cho API call
            handler.postDelayed(apiCallRunnable, API_CALL_INTERVAL);
        }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Dừng các Runnable khi Service bị destroy
        handler.removeCallbacks(reminderRunnable);
        handler.removeCallbacks(apiCallRunnable);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showReminderNotification() {
        String reminderTxt="none";
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        if (stateWeather==false) {  //Nếu như trời mưa
            reminderTxt = getString(R.string.hey_it_s_going_to_rain_today_remember_to_bring_an_umbrella_when_going_out_and_be_careful_of_slippery_roads);
        }
        else {
            reminderTxt = getString(R.string.hey_it_s_going_to_be_sunny_today_let_s_go_out_and_enjoy_it);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),chanelID);
        builder.setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle("Reminder Weather")
                .setContentText(reminderTxt)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(getApplicationContext(), FragmentHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager)  getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel(chanelID);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID,
                        "Some description",importance);
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

                            if (humidity >= 80 || rainfall >= 16 || wind >= 7.2) {
                                stateWeather = false; //Trời mưa
                            } else {
                                stateWeather = true; //Trời nắng
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