package com.example.natural;



import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ResetPassword extends AppCompatActivity {
    TextView tv_login;
    EditText edt_user;
    Button submitBtn;
    ImageView languageIcon,backIcon;
    WebView webViewReset;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        tv_login = findViewById(R.id.loginTxt);
        submitBtn = findViewById(R.id.submitBtn);
        edt_user = findViewById(R.id.edtUsr);
        languageIcon = findViewById(R.id.languageIcon);
        webViewReset = findViewById(R.id.webViewReset);
        backIcon = findViewById(R.id.left_arrow);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPassword.this,MainPageActivity.class);
                startActivity(intent);
            }
        });
        languageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPwd();
            }
        });

    }

    private void showChangeLanguageDialog() {
        //array of language to display in alert dialog
        final String[] listItems = {"Tiếng Việt", "English"};
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(ResetPassword.this);
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

    private void resetPwd() {
        if (isEmptyInput()) {
            return;
        }
        gotoLoginPage();
    }

    private boolean isEmptyInput() {
        String strUser = edt_user.getText().toString();
        if (strUser.isEmpty()) {
            ShowError(edt_user,getString(R.string.please_fill_out_this_field));
            return true;
        }
        return false;
    }

    private void gotoLoginPage() {
        Intent intent = new Intent(ResetPassword.this,LogInActivity.class);
        Toast.makeText(ResetPassword.this,getString(R.string.received_email),Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private void ShowError(EditText edtInput, String s) {
        edtInput.setError(s);
        edtInput.requestFocus();
    }
}