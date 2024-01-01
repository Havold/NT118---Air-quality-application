package com.example.natural;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.natural.api.apiService_token;
import com.example.natural.model.LoadingAlert;
import com.example.natural.model.token;
import com.example.natural.shared_preference.PreferenceUtils;

import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    ImageView languageIcon;
    LoadingAlert loadingAlert;
    CheckBox checkRemember;
    SharedPreferences sharedPreferences;
    TextView tv_forget;
    EditText user,pwd;
    SharedPreferences.Editor editor;
    private Animation.AnimationListener animationListener;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Trong phương thức onCreate
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);

        // Nhận một đối tượng Editor để chỉnh sửa dữ liệu
        editor = sharedPreferences.edit();

        TextView signup = findViewById(R.id.signupTxt);
        ImageView back = findViewById(R.id.left_arrow);
        Button signinBtn = findViewById(R.id.SignInBtn);
        user = findViewById(R.id.edtUsr);
        pwd = findViewById(R.id.edtPwd);
        languageIcon = findViewById(R.id.languageIcon);
        loadingAlert = new LoadingAlert(LogInActivity.this);
        checkRemember = findViewById(R.id.checkRemember);
        tv_forget = findViewById(R.id.forgetTxt);

        handleButtonAnimationXml(signinBtn,R.anim.anim_zoom_in);
        handleImageAnimationXml(languageIcon,R.anim.anim_zoom_in);

        // Kiểm tra trạng thái "Remember Me"
        if (rememberMe) {
            String username = sharedPreferences.getString("username", "");
            String password = sharedPreferences.getString("password", "");

            // Điền thông tin đăng nhập vào các trường
            user.setText(username);
            pwd.setText(password);
        }


        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,ResetPassword.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this,MainPageActivity.class);
                startActivity(intent);
            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingAlert.StartAlertDialog();
                String userStr = String.valueOf(user.getText());
                String pwdStr = String.valueOf(pwd.getText());
                loginApi(userStr,pwdStr);
            }
        });

        languageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });
    }

    private void loginApi(String user, String password){
        apiService_token.api_token.getLoginToken("openremote", user, password, "password").enqueue(new Callback<token>() {
            @Override
            public void onResponse(@NonNull Call<token> call, @NonNull Response<token> response) {
                Log.d("API CALL", response.code()+"");
                if (response.code() == 200) {
                    //save token
                    if (response.body() != null) {
                        String token = response.body().getAccess_token();
                        PreferenceUtils.saveToken(token);
                        loadingAlert.CloseAlertDialog();

                        // Lưu trạng thái "Remember Me"
                        boolean rememberMe = checkRemember.isChecked();
                        editor.putBoolean("rememberMe", rememberMe);
                        editor.apply();

                        // Nếu "Remember Me" được chọn
                        if (rememberMe) {
                            // Lưu thông tin đăng nhập
                            editor.putString("username", user);
                            editor.putString("password", password);
                            editor.apply();
                        }

                        Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                        intent.putExtra("accessToken", token);
                        startActivity(intent);
                    }
                } else {
                    showAlertDialog();
                }
            }
            @Override
            public void onFailure(@NonNull Call<token> call, @NonNull Throwable t) {
                Log.d("API CALL", Objects.requireNonNull(t.getMessage()));
                showAlertDialog();
            }
        });


    }

    public void showAlertDialog() {
        EditText pwd = findViewById(R.id.edtPwd);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        loadingAlert.CloseAlertDialog();

        builder.setTitle(getString(R.string.wrong_password))
                .setMessage(R.string.the_password_you_just_entered_is_not_correct_please_re_enter)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pwd.setText("");
                    }
                })
                .show();
    }

    private void showChangeLanguageDialog() {
        //array of language to display in alert dialog
        final String[] listItems = {"Tiếng Việt", "English"};
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(LogInActivity.this);
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    setLocale("VI",languageIcon);
                    recreate();
                } else if (i == 1) {
                    setLocale("EN",languageIcon);
                    recreate();
                }
            }
        });
        androidx.appcompat.app.AlertDialog mDialog = mBuilder.create();
        //show alert dialog
        mDialog.show();
    }

    private void setLocale(String lang,ImageView languageIcon) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    private void initVariables() {
        animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };
    }

    private void handleButtonAnimationXml(Button btn, int animId)
    {
        // HandleClickAnimationXML
        // Load the Animation
        final Animation animation = AnimationUtils.loadAnimation(LogInActivity.this,animId);

        // set animation listener
        animation.setAnimationListener(animationListener);

        btn.startAnimation(animation);
    }

    private void handleImageAnimationXml(ImageView img, int animId)
    {
        // HandleClickAnimationXML
        // Load the Animation
        final Animation animation = AnimationUtils.loadAnimation(LogInActivity.this,animId);

        // set animation listener
        animation.setAnimationListener(animationListener);

        img.startAnimation(animation);
    }
}