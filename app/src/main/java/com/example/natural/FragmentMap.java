package com.example.natural;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.natural.api.apiService_token;
import com.example.natural.model.MapResponse;
import com.example.natural.model.SharedViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentMap extends Fragment implements OnMapReadyCallback {
    GoogleMap gMap;
    FrameLayout map;
    Button showBtn;
    SharedViewModel sharedViewModel;
    String accessToken;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        map = view.findViewById(R.id.map);

        //         Khởi tạo ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        getMapOption(gMap);
//
//        LatLng HCM = new LatLng( 9.812116855723476, 105.82270937360353 );
//        this.gMap.addMarker(new MarkerOptions().position(HCM).title("Trường Đại Học Công Nghệ Thông Tin"));
//        gMap.getUiSettings().setZoomControlsEnabled(true);
//        gMap.getUiSettings().setCompassEnabled(true);
//        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(HCM));
    }

    private void getMapOption(GoogleMap gMap) {
        apiService_token.api_token.getMapData("").enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                Toast.makeText(requireContext(),"Call API Success", Toast.LENGTH_SHORT).show();

                MapResponse mapResponse = response.body();

                if (mapResponse!=null) {
                    addDataOnMap(gMap,mapResponse);
                }

            }

            @Override
            public void onFailure(Call<MapResponse> call, Throwable t) {
                Toast.makeText(requireContext(),"Call API Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDataOnMap(GoogleMap gMap, MapResponse mapResponse) {
//        double center_x,center_y;
//        center_x=mapResponse.getOptions().getDefaultOptions().getCenter()[0];
//        center_y=mapResponse.getOptions().getDefaultOptions().getCenter()[1];

        // Tạo các LatLng cho 3 điểm
        LatLng Weather = new LatLng(10.869778736885038, 106.80280655508835);
        LatLng Light = new LatLng(10.869905172970164, 106.80345028525176);
        LatLng UIT = new LatLng((Double) mapResponse.getOptions().getDefaultOptions().getCenter()[1],(Double) mapResponse.getOptions().getDefaultOptions().getCenter()[0]);

        // Thêm Marker cho từng điểm
        this.gMap.addMarker(new MarkerOptions()
                .position(Weather)
                .title("Default Weather")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sunny_mini)));
        this.gMap.addMarker(new MarkerOptions()
                .position(Light)
                .title("Light")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_mini)));
        this.gMap.addMarker(new MarkerOptions().position(UIT).title("Trường Đại học Công nghệ Thông tin"));

        // Cài đặt zoom và di chuyển camera đến vùng chứa các điểm
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(Weather);
        builder.include(Light);
        builder.include(UIT);
        LatLngBounds bounds = builder.build();

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                openInforDialog();
                return true;
            }
        });

        int padding = 50; // Padding để đảm bảo tất cả các Marker đều nhìn thấy
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        this.gMap.moveCamera(cameraUpdate);

        // Cài đặt các tùy chọn UI khác nếu cần
        this.gMap.getUiSettings().setZoomControlsEnabled(true);
        this.gMap.getUiSettings().setCompassEnabled(true);



//        LatLngBounds UITBounds = new LatLngBounds(
//                new LatLng((Double) mapResponse.getOptions().getDefaultOptions().getBounds()[1],(Double) mapResponse.getOptions().getDefaultOptions().getBounds()[0]),
//                new LatLng((Double) mapResponse.getOptions().getDefaultOptions().getBounds()[3],(Double) mapResponse.getOptions().getDefaultOptions().getBounds()[2])
//        );
//
//        //add Maker
//        gMap.addMarker(new MarkerOptions().position(UIT).title("Trường Đại học Công nghệ Thông tin"));
//        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UIT, (float) mapResponse.getOptions().getDefaultOptions().getMaxZoom()-2));
//        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(UIT, (float)mapResponse.getOptions().getDefaultOptions().getMaxZoom()-2));
//        gMap.getUiSettings().setZoomControlsEnabled(true);
//        gMap.getUiSettings().setCompassEnabled(true);
//        gMap.setLatLngBoundsForCameraTarget(UITBounds);

    }

    private void openInforDialog() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.infor_dialog_weather);

        TextView tempTxt = dialog.findViewById(R.id.tempTxt);
        TextView rainfallTxt = dialog.findViewById(R.id.rainfallTxt);
        TextView humidTxt = dialog.findViewById(R.id.humidityTxt);
        TextView windTxt = dialog.findViewById(R.id.windspeedTxt);

        float temp = sharedViewModel.getTemperature();
        float rainfall = sharedViewModel.getRainfall();
        float humid = sharedViewModel.getHumidity();
        float wind = sharedViewModel.getWind();


        // Thiết lập giá trị cho các TextView
        tempTxt.setText(String.valueOf(temp));
        rainfallTxt.setText(String.valueOf(rainfall) + "mm");
        humidTxt.setText(String.valueOf(humid)+"%");
        windTxt.setText(String.valueOf(wind)+"km/h");


        Window window = dialog.getWindow();
        if (window==null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity= Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        showBtn = (Button) dialog.findViewById(R.id.showBtn);
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gotoHomeFragment();


//                // Cập nhật item trong BottomNavigationView
//                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
//                bottomNavigationView.setSelectedItemId(R.id.bottom_home);
            }
        });
        dialog.show();
    }

    private void gotoHomeFragment() {
        Fragment homeFragment = new FragmentHome();
        FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.frameLayout,homeFragment).commit();
    }

}