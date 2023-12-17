package com.example.natural;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static androidx.core.app.ActivityCompat.recreate;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.natural.api.apiService_token;
import com.example.natural.model.DateUtils;
import com.example.natural.model.SharedViewModel;
import com.example.natural.model.WeatherResponse;
import com.example.natural.SQLite.DatabaseHelper;
import com.example.natural.service.MyJobService;
import com.example.natural.service.MyService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentHome extends Fragment {
    private static final int JOB_ID = 123;
    private boolean onNotification = false;
    ImageView userIcon;
    String assetID= "5zI6XqkQVSfdgOrZ1MyWEf";
    String accessToken;
    ImageView pointYellow,pointWhite,locationBlue,locationRed,languageIcon,weatherIcon,notificationIcon,notificationIconOff,strokeWhite,strokeFull;

    SharedViewModel sharedViewModel;
    TextView tv_update,tv_place,tv_temp,tv_wind,tv_humidity,tv_rainfall,tv_date,tv_weather;
    apiService_token apiServiceToken;
    TextView tv_windTitle, tv_humidityTitle, tv_rainfallTitle;
    boolean stateWeather,stateNight;

    ImageView night_bg,rectangle_smooth;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(requireActivity(),
                        new String[] {Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }

//         Khởi tạo ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
//         Nhận dữ liệu từ ViewModel
        accessToken = sharedViewModel.getAccessToken();
        onNotification = sharedViewModel.isOnNotification();



        tv_place = view.findViewById(R.id.locationTxt);
        tv_temp = view.findViewById(R.id.temperate);
        tv_wind = view.findViewById(R.id.windTxt);
        tv_humidity = view.findViewById(R.id.humidityTxt);
        tv_rainfall = view.findViewById(R.id.rainfallTxt);
        tv_date = view.findViewById(R.id.date);
        userIcon = view.findViewById(R.id.userIcon);
        languageIcon = view.findViewById(R.id.languageIcon);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        tv_weather = view.findViewById(R.id.weatherTxt);
        notificationIcon = view.findViewById(R.id.notification);
        notificationIconOff = view.findViewById(R.id.notification_off);
        night_bg = view.findViewById(R.id.img_night);
        rectangle_smooth= view.findViewById(R.id.rectangle_smooth);
        tv_windTitle = view.findViewById(R.id.windTitleTxt);
        tv_humidityTitle = view.findViewById(R.id.humidityTitleTxt);
        tv_rainfallTitle = view.findViewById(R.id.rainfallTitleTxt);
        strokeWhite = view.findViewById(R.id.whiteStroke);
        strokeFull = view.findViewById(R.id.whiteStrokeFull);
        tv_update = view.findViewById(R.id.status);
        locationBlue = view.findViewById(R.id.locationIcon);
        locationRed = view.findViewById(R.id.locationIconRed);
        pointWhite = view.findViewById(R.id.whitePoint);
        pointYellow = view.findViewById(R.id.yellowPoint);

        if (onNotification) {
            notificationIcon.setVisibility(View.VISIBLE);
            notificationIconOff.setVisibility(View.GONE);
        }
        else {
            notificationIconOff.setVisibility(View.VISIBLE);
            notificationIcon.setVisibility(View.GONE);
        }

        if (accessToken!=null) {
            callWeatherAPI(accessToken);
        }





//        // Nhận dữ liệu từ Bundle
//        if (getArguments() != null) {
//            accessToken = getArguments().getString("accessToken");
//            // Sử dụng thông tin accessToken ở đây
//            // Ví dụ:
//            callWeatherAPI(accessToken);
//        }
//
//        Fragment fragmentMap = new FragmentMap();
//        Fragment fragmentGraph = new FragmentGraph();
//        Fragment fragmentProfile = new FragmentProfile();
//
//        // Tạo Bundle và đặt thông tin vào Bundle
//        Bundle bundle = new Bundle();
//        bundle.putString("accessToken", accessToken);
//
//
//        // Gán Bundle cho Fragment
//        fragmentMap.setArguments(bundle);
//        fragmentGraph.setArguments(bundle);
//        fragmentProfile.setArguments(bundle);

//        fragmentProfile.setArguments(bundle);

        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(),"Reminder off!", Toast.LENGTH_SHORT).show();
                notificationIcon.setVisibility(View.GONE);
                notificationIconOff.setVisibility(View.VISIBLE);
                sharedViewModel.setOnNotification(false);
//                stopReminderService();
                onClickStopScheduleJob();
            }
        });

        notificationIconOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(),"Reminder on!", Toast.LENGTH_SHORT).show();
                notificationIconOff.setVisibility(View.GONE);
                notificationIcon.setVisibility(View.VISIBLE);
                sharedViewModel.setOnNotification(true);
                Intent serviceIntent = new Intent(requireContext(), MyJobService.class);
                serviceIntent.putExtra("stateNight",stateNight);
                serviceIntent.putExtra("stateWeather",stateWeather);
                serviceIntent.putExtra("assetID",assetID);
                serviceIntent.putExtra("accessToken",accessToken);
                requireContext().startService(serviceIntent);

                // startReminderService();
                onClickStartScheduleJob();
            }

        });

        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfileFragment();

            }
        });

        return view;
    }


    private void callWeatherAPI(String accessToken) {

        apiServiceToken.api_token
                .getAsset(assetID,"Bearer " + accessToken)
                .enqueue(new Callback<WeatherResponse>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        Toast.makeText(requireContext(),"Call API Success",Toast.LENGTH_SHORT).show();
                        WeatherResponse weatherResponse = response.body();
                        if (weatherResponse!=null) {
                            float rainfall,humidity,temp,wind;
                            long timestamp;
                            rainfall = weatherResponse.getAttributes().getRainfall().getValue();
                            humidity = weatherResponse.getAttributes().getHumidity().getValue();
                            temp = weatherResponse.getAttributes().getTemperature().getValue();
                            wind = weatherResponse.getAttributes().getWindSpeed().getValue();
                            timestamp = weatherResponse.getCreatedOn();
                            tv_place.setText(weatherResponse.getAttributes().getPlace().getValue());
                            tv_rainfall.setText(String.valueOf(rainfall)+"mm");
                            tv_humidity.setText(String.valueOf(humidity)+"%");
                            tv_temp.setText(String.valueOf(temp));
                            tv_wind.setText(String.valueOf(wind)+"km/h");
                            tv_date.setText(String.valueOf(convertTimestampToFormattedDate(timestamp)));

                            // Truyền dữ liệu vào ViewModel khi cần
                            sharedViewModel.setTemperature(temp);
                            sharedViewModel.setRainfall(rainfall);
                            sharedViewModel.setHumidity(humidity);
                            sharedViewModel.setWind(wind);

                            int hour = DateUtils.convertTimestampToHourInt(timestamp);
//                            int hour=6;
//                            humidity=14;

                            if (hour>=18 || hour<=4) {  //Trời tối
                                stateNight=true;

                                night_bg.setVisibility(View.VISIBLE);
                                rectangle_smooth.setVisibility(View.GONE);

                                tv_rainfall.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                                tv_humidity.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                                tv_wind.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                                tv_place.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                                tv_date.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                                tv_weather.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                                tv_humidityTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                                tv_rainfallTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                                tv_windTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

                                tv_update.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

                                pointWhite.setVisibility(View.VISIBLE);
                                pointYellow.setVisibility(View.GONE);

                                locationBlue.setVisibility(View.VISIBLE);
                                locationRed.setVisibility(View.GONE);

                                strokeWhite.setVisibility(View.GONE);
                                strokeFull.setVisibility(View.VISIBLE);

                                if (humidity>=80 || rainfall>= 16 || wind>=7.2) {
                                    Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.chance_of_rain_predict);
                                    weatherIcon.setBackground(drawable);
                                    tv_weather.setText(R.string.rainy);
                                    stateWeather=false; //Trời mưa
                                }
                                else {
                                    Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.moon);
                                    weatherIcon.setBackground(drawable);
                                    tv_weather.setText(R.string.moon_and_star);
                                    stateWeather=true; //Trời trăng
                                }
                            }
                            else {  //Trời sáng
                                stateNight=false;

                                night_bg.setVisibility(View.GONE);
                                rectangle_smooth.setVisibility(View.VISIBLE);

                                tv_rainfall.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                                tv_humidity.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                                tv_wind.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                                tv_place.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                                tv_date.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                                tv_weather.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                                tv_humidityTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));
                                tv_rainfallTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));
                                tv_windTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));

                                tv_update.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

                                locationBlue.setVisibility(View.GONE);
                                locationRed.setVisibility(View.VISIBLE);

                                locationBlue.setVisibility(View.GONE);
                                locationRed.setVisibility(View.VISIBLE);

                                strokeWhite.setVisibility(View.VISIBLE);
                                strokeFull.setVisibility(View.GONE);


                                if (humidity>=80 || rainfall>= 16 || wind>=7.2) {
                                    Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.chance_of_rain_predict);
                                    weatherIcon.setBackground(drawable);
                                    tv_weather.setText(R.string.rainy);
                                    stateWeather=false; //Trời mưa
                                }
                                else {
                                    Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.sunny);
                                    weatherIcon.setBackground(drawable);
                                    tv_weather.setText(R.string.sunny);
                                    stateWeather=true; //Trời nắng
                                }
                            }



                            //        Khai báo để sử dụng SQLite
                            DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
                            dbHelper.insertWeatherData(temp, humidity, wind, rainfall, timestamp);
                            dbHelper.close();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        Toast.makeText(requireContext(),"Call API Error",Toast.LENGTH_SHORT).show();
                    }
                });

        languageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });

    }

    public static String convertTimestampToFormattedDate(long timestamp) {
        try {
            // Tạo đối tượng Date từ timestamp
            Date date = new Date(timestamp);

            // Định dạng ngày giờ
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMM, yyyy", Locale.getDefault());

            // Chuyển đổi Date thành chuỗi định dạng
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void gotoProfileFragment() {
        Fragment profileFragment = new FragmentProfile();
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.frameLayout,profileFragment).commit();
    }

    private void showChangeLanguageDialog() {
        // array of language to display in alert dialog
        final String[] listItems = {"Tiếng Việt", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(requireContext());
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    setLocale("vi", languageIcon);
                } else if (i == 1) {
                    setLocale("en", languageIcon);
                }

                // Dismiss the dialog after choosing a language
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        // show alert dialog
        mDialog.show();
    }

    private void setLocale(String lang, ImageView languageIcon) {
        // Update the locale configuration
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Save the selected language to SharedPreferences
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();

        // Recreate the activity to apply the language change
        requireActivity().recreate();
    }

    private void startReminderService() {
        Intent serviceIntent = new Intent(requireContext(), MyService.class);
        serviceIntent.putExtra("stateWeather",stateWeather);
        serviceIntent.putExtra("assetID",assetID);
        serviceIntent.putExtra("accessToken",accessToken);
        requireContext().startService(serviceIntent);
    }

    private void stopReminderService() {
        Intent serviceIntent = new Intent(requireContext(), MyService.class);
        requireContext().stopService(serviceIntent);
    }

    private void onClickStartScheduleJob() {
        ComponentName componentName = new ComponentName(requireActivity(), MyJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID,componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15*60*1000)
                .build();
        JobScheduler jobScheduler = (JobScheduler) requireContext().getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
    }

    private void onClickStopScheduleJob() {
        JobScheduler jobScheduler = (JobScheduler) requireContext().getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
    }
}