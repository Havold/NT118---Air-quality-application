package com.example.natural;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.natural.api.apiService_token;
import com.example.natural.model.WeatherResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentHome extends Fragment {
    TextView tv_place,tv_temp,tv_wind,tv_humidity,tv_rainfall,tv_date;
    apiService_token apiServiceToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tv_place=view.findViewById(R.id.locationTxt);
        tv_temp=view.findViewById(R.id.temperate);
        tv_wind=view.findViewById(R.id.windTxt);
        tv_humidity=view.findViewById(R.id.humidityTxt);
        tv_rainfall=view.findViewById(R.id.rainfallTxt);
        tv_date=view.findViewById(R.id.date);
        // Nhận dữ liệu từ Bundle
        if (getArguments() != null) {
            String accessToken = getArguments().getString("accessToken");
            // Sử dụng thông tin accessToken ở đây
            // Ví dụ:
            callWeatherAPI(accessToken);
        }

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
                            tv_place.setText(weatherResponse.getAttributes().getPlace().getValue());
                            tv_rainfall.setText(String.valueOf(weatherResponse.getAttributes().getRainfall().getValue())+"mm");
                            tv_humidity.setText(String.valueOf(weatherResponse.getAttributes().getHumidity().getValue())+"%");
                            tv_temp.setText(String.valueOf(weatherResponse.getAttributes().getTemperature().getValue()));
                            tv_wind.setText(String.valueOf(weatherResponse.getAttributes().getWindSpeed().getValue())+"km/h");
                            tv_date.setText(String.valueOf(convertTimestampToFormattedDate(weatherResponse.getCreatedOn())));
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        Toast.makeText(requireContext(),"Call API Error",Toast.LENGTH_SHORT).show();
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
}