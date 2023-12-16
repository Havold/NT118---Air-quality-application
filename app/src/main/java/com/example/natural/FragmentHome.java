package com.example.natural;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.app.ActivityCompat.recreate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.natural.api.apiService_token;
import com.example.natural.model.SharedViewModel;
import com.example.natural.model.WeatherResponse;
import com.example.natural.SQLite.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentHome extends Fragment {
    ImageView userIcon;
    ImageView languageIcon;

    SharedViewModel sharedViewModel;
    TextView tv_place,tv_temp,tv_wind,tv_humidity,tv_rainfall,tv_date;
    apiService_token apiServiceToken;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//         Khởi tạo ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
//         Nhận dữ liệu từ ViewModel
        String accessToken = sharedViewModel.getAccessToken();

        tv_place = view.findViewById(R.id.locationTxt);
        tv_temp = view.findViewById(R.id.temperate);
        tv_wind = view.findViewById(R.id.windTxt);
        tv_humidity = view.findViewById(R.id.humidityTxt);
        tv_rainfall = view.findViewById(R.id.rainfallTxt);
        tv_date = view.findViewById(R.id.date);
        userIcon = view.findViewById(R.id.userIcon);
        languageIcon = view.findViewById(R.id.languageIcon);

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
                .getAsset("5zI6XqkQVSfdgOrZ1MyWEf","Bearer " + accessToken)
                .enqueue(new Callback<WeatherResponse>() {
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

                            //        Khai báo để sử dụng SQLite
                            DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
//                            dbHelper.deleteTable();
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

}