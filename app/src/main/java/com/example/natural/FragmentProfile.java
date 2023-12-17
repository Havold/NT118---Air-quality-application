package com.example.natural;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.natural.api.apiService_token;
import com.example.natural.model.FragmentInteractionListener;
import com.example.natural.model.SharedViewModel;
import com.example.natural.model.UserResponse;
import com.example.natural.model.WeatherResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfile extends Fragment {
    private SharedViewModel sharedViewModel;
    Button logoutBtn;
    TextView tv_name,tv_username,tv_email,tv_date,tv_password;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    apiService_token apiServiceToken;
    boolean stateGoogle;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Khởi tạo ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        // Nhận dữ liệu từ ViewModel
        String accessToken = sharedViewModel.getAccessToken();


        logoutBtn = view.findViewById(R.id.logoutBtn);
        tv_date = view.findViewById(R.id.timeTxt);
        tv_name = view.findViewById(R.id.nameTxt);
        tv_email = view.findViewById(R.id.mailTxt);
        tv_username = view.findViewById(R.id.usernameTxt);
        tv_password = view.findViewById(R.id.pwdTxt);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(requireContext(),gso);

        if (accessToken!=null) {
            callUserAPI(accessToken);
        }

//        // Lấy Bundle từ Fragment
//        Bundle bundle = getArguments();
//
//        // Nhận dữ liệu từ Bundle
//        if (bundle != null) {
//            String accessToken = getArguments().getString("accessToken");
//            // Sử dụng thông tin accessToken ở đây
//            // Ví dụ:
//            callUserAPI(accessToken);
//        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut();

//                else {
//                    Intent intent = new Intent(requireContext(), LogInActivity.class);
//                    startActivity(intent);
//                }
            }
        });
        return view;
    }

    private void callUserAPI(String accessToken) {
        apiServiceToken.api_token
                .getUser("Bearer " + accessToken)
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        Toast.makeText(requireContext(),"Call API Success",Toast.LENGTH_SHORT).show();
                        UserResponse userResponse = response.body();
                        if (userResponse!=null) {
                            tv_username.setText(userResponse.getUsername());
                            tv_email.setText(userResponse.getEmail());
                            tv_name.setText(userResponse.getFirstName()+" "+userResponse.getLastName());
                            tv_date.setText("Created on "+convertTimestampToFormattedDate(userResponse.getCreatedOn()));
                        }

                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(requireContext(),"Call API Error",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static String convertTimestampToFormattedDate(long timestamp) {
        try {
            // Tạo đối tượng Date từ timestamp
            Date date = new Date(timestamp);

            // Định dạng ngày giờ
            SimpleDateFormat sdf = new SimpleDateFormat("d MMM, yyyy", Locale.getDefault());

            // Chuyển đổi Date thành chuỗi định dạng
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (getActivity() instanceof FragmentInteractionListener) {
                    ((FragmentInteractionListener) getActivity()).onSignOut();
                }
            }
        });
    }
}