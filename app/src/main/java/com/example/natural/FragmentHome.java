package com.example.natural;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class FragmentHome extends Fragment implements OnMapReadyCallback {
    GoogleMap gMap;
    FrameLayout map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        map = view.findViewById(R.id.map);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;

        LatLng HCM = new LatLng( 9.812116855723476, 105.82270937360353 );
        this.gMap.addMarker(new MarkerOptions().position(HCM).title("Trường Đại Học Công Nghệ Thông Tin"));
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);
        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(HCM));
    }
}