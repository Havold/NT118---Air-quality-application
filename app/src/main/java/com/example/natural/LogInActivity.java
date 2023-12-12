package com.example.natural;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.natural.api.apiService_token;
import com.example.natural.model.LoadingAlert;
import com.example.natural.model.token;
import com.example.natural.shared_preference.PreferenceUtils;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    ImageView languageIcon;
    LoadingAlert loadingAlert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        TextView signup = findViewById(R.id.signupTxt);
        ImageView back = findViewById(R.id.left_arrow);
        Button signinBtn = findViewById(R.id.SignInBtn);
        EditText user = findViewById(R.id.edtUsr);
        EditText pwd = findViewById(R.id.edtPwd);
        languageIcon = findViewById(R.id.languageIcon);
        loadingAlert = new LoadingAlert(LogInActivity.this);

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
                        Intent intent = new Intent(LogInActivity.this,MainActivity2.class);
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

        builder.setTitle("Wrong password")
                .setMessage("The password you just entered is not correct. Please re-enter.")
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
}