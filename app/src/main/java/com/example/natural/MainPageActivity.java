package com.example.natural;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.natural.model.SharedViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.util.Locale;


public class MainPageActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button googleBtn;
    ImageView languageIcon,logo;
    TextView tv_forget;
    SharedViewModel sharedViewModel;
    private Animation.AnimationListener animationListener;
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        googleBtn = findViewById(R.id.LogInGoogle);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        tv_forget = findViewById(R.id.forgetTxt);
        Button signIn_btn = (Button) findViewById((R.id.signIn_btn));
        Button signUp_btn = (Button) findViewById(R.id.SignUpBtn);
        languageIcon = (ImageView) findViewById(R.id.languageIcon);
        logo = findViewById(R.id.logo);

        initVariables();

        // HandleClickAnimationXML
        handleImageAnimationXml(logo, R.anim.anim_zoom_in);
//        handleImageAnimationXml(logo, R.anim.anim_zoom_out);
        handleButtonAnimationXml(signIn_btn, R.anim.anim_fade_in);
        handleButtonAnimationXml(signUp_btn, R.anim.anim_fade_in);
        handleButtonAnimationXml(googleBtn, R.anim.anim_fade_in);
        handleImageAnimationXml(languageIcon,R.anim.anim_zoom_in);


        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        languageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });

        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this,ResetPassword.class);
                startActivity(intent);
            }
        });
    }

    void signInGoogle() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void navigateToSecondActivity() {
        finish();
        Intent intent = new Intent(MainPageActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void showChangeLanguageDialog() {
        //array of language to display in alert dialog
        final String[] listItems = {"Tiếng Việt", "English"};
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(MainPageActivity.this);
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
        final Animation animation = AnimationUtils.loadAnimation(MainPageActivity.this,animId);

        // set animation listener
        animation.setAnimationListener(animationListener);

        btn.startAnimation(animation);
    }

    private void handleImageAnimationXml(ImageView img, int animId)
    {
        // HandleClickAnimationXML
        // Load the Animation
        final Animation animation = AnimationUtils.loadAnimation(MainPageActivity.this,animId);

        // set animation listener
        animation.setAnimationListener(animationListener);

        img.startAnimation(animation);
    }
}
