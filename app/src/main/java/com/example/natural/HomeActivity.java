package com.example.natural;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView name,email;
    Button signOutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Trong HomeActivity
        FragmentHome fragmentHome = new FragmentHome();

        // Nhận dữ liệu
        Intent intent = getIntent();
        String accessToken = intent.getStringExtra("accessToken");

        // Tạo Bundle và đặt thông tin vào Bundle
        Bundle bundle = new Bundle();
        bundle.putString("accessToken", accessToken);

        // Gán Bundle cho Fragment
        fragmentHome.setArguments(bundle);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPage);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        bottomNavigationView =  findViewById(R.id.bottomNav);
        frameLayout = findViewById(R.id.frameLayout);

        viewPager2.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
        if (savedInstanceState == null) {
            // Add FragmentMap if there is no saved instance state
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragmentHome)
                    .commit();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager2.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
            }

        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                        tabLayout.getTabAt(position).select();
                }
                super.onPageSelected(position);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                frameLayout.setVisibility(View.VISIBLE);
                viewPager2.setVisibility(View.GONE);
                if (item.getItemId()==R.id.bottom_home) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new FragmentHome()).commit();
                    return true;
                }
                else if (item.getItemId()==R.id.bottom_map) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new FragmentMap()).commit();
                    return true;
                }
                else if (item.getItemId()==R.id.bottom_graph) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new FragmentGraph()).commit();
                    return true;
                }
                return false;
            }
        });



//        signOutBtn = findViewById(R.id.signOutBtn);
//        name = findViewById(R.id.name);
//        email = findViewById(R.id.email);

//        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        gsc= GoogleSignIn.getClient(this,gso);
//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
//        if (acct!=null) {
//            String personName = acct.getDisplayName();
//            String personEmail = acct.getEmail();
//            name.setText(personName);
//            email.setText(personEmail);
//        }
//
//        signOutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signOut();
//            }
//
//            private void signOut() {
//                gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        finish();
//                        Intent intent = new Intent(HomeActivity.this,MainPageActivity.class);
//                        startActivity(intent);
//                    }
//                });
//            }
//        });
    }
}