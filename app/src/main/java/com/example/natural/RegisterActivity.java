package com.example.natural;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.natural.model.LoadingAlert;
import com.example.natural.model.URL;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    private boolean isFormSubmitted = false;
    private Handler handler;

    WebView webViewSignUp;
    URL url;
    LoadingAlert loadingAlert;
    private EditText edtName, edtEmail, edtPass, edtConfirmPass;
    private  Button signupBtn;
    ImageView languageIcon;
    private Animation.AnimationListener animationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        LoadElement();
        TextView login = findViewById(R.id.LogIn);
        ImageView back = findViewById(R.id.LeftArrow);
        languageIcon = findViewById(R.id.languageIcon);


        handleImageAnimationXml(languageIcon,R.anim.anim_zoom_in);
        handleButtonAnimationXml(signupBtn,R.anim.anim_zoom_in);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LogInActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,MainPageActivity.class);
                startActivity(intent);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });

        languageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void SignUp() {
        String usr = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String pwd = edtPass.getText().toString();
        String rePwd= edtConfirmPass.getText().toString();

        if (isEmptyInput()) return;

        loadingAlert.StartAlertDialog();
        webViewSignUp.getSettings().setJavaScriptEnabled(true);
        webViewSignUp.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("uiot.ixxc.dev/manager/")&&!isFormSubmitted ) {
                    loadingAlert.CloseAlertDialog();
                    Toast.makeText(RegisterActivity.this, getString(R.string.your_registration_was_succesful),Toast.LENGTH_SHORT).show();
                    isFormSubmitted=true;
                    Intent intent = new Intent(RegisterActivity.this,LogInActivity.class);
                    startActivity(intent);
                }
                else if (url.contains("openid-connect/registrations")&&!isFormSubmitted) {
                    handler.postDelayed((Runnable) () -> {
                        if (!isFormSubmitted) {
                            loadingAlert.CloseAlertDialog();
                            showAlert("REGISTRATION FAILED", getString(R.string.username_or_email_already_exists),false);
                        }
                    },3000 );
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.contains("openid-connect/registrations")) {
                    String usrScript = "document.getElementById('username').value ='" + usr + "';";
                    String emailScript = "document.getElementById('email').value ='" + email + "';";
                    String pwdScript = "document.getElementById('password').value ='" + pwd + "';";
                    String rePwdScript = "document.getElementById('password-confirm').value ='" + rePwd + "';";
                    String submitFormScript = "document.querySelector('form').submit();";

                    webViewSignUp.evaluateJavascript(usrScript, null);
                    webViewSignUp.evaluateJavascript(emailScript, null);
                    webViewSignUp.evaluateJavascript(pwdScript, null);
                    webViewSignUp.evaluateJavascript(rePwdScript, null);
                    webViewSignUp.evaluateJavascript(submitFormScript, null);

                }
            }
        });
        webViewSignUp.setVisibility(View.GONE);
        //String url = "https://uiot.ixxc.dev/auth/realms/master/protocol/openid-connect/registrations?client_id=openremote&redirect_uri=https%3A%2F%2Fuiot.ixxc.dev%2Fmanager%2F&response_mode=fragment&response_type=code&scope=openid";
        webViewSignUp.loadUrl(url.GetURLFormSignUp());
    }

    private boolean isEmptyInput() {
        String strEmail = edtEmail.getText().toString();
        if (edtName.getText().toString().isEmpty()) {
            ShowError(edtName,getString(R.string.what_s_your_name));
            return true;
        } else if (edtEmail.getText().toString().isEmpty()) {
            ShowError(edtEmail,getString(R.string.please_enter_your_email));
            return true;
        } else if (!isValidEmail(strEmail)) {
            ShowError(edtEmail,getString(R.string.please_enter_a_valid_email));
            return true;
        }
        else if (edtPass.getText().toString().isEmpty()) {
            ShowError(edtPass,getString(R.string.enter_password));
            return true;
        } else if (edtConfirmPass.getText().toString().isEmpty()) {
            ShowError(edtConfirmPass,getString(R.string.please_confirm_your_password));
            return true;
        }
        return false;
    }

    private void ShowError(EditText edtInput, String s) {
        edtInput.setError(s);
        edtInput.requestFocus();
    }

    private void LoadElement() {
        url = new URL();
        loadingAlert = new LoadingAlert(RegisterActivity.this);
        signupBtn = findViewById(R.id.SignUpBtn);
        webViewSignUp = findViewById(R.id.webViewRegister);
        edtName = findViewById(R.id.edtUsr);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPwd);
        edtConfirmPass = findViewById(R.id.edtCfPwd);
        handler = new Handler();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        WebSettings webSettings = webViewSignUp.getSettings();
        webSettings.setDomStorageEnabled(true);
        cookieManager.removeAllCookies(null);
        cookieManager.flush();
    }

    private void showAlert(String title, String message, boolean error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    public boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void showChangeLanguageDialog() {
        //array of language to display in alert dialog
        final String[] listItems = {"Tiếng Việt", "English"};
        androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(RegisterActivity.this);
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
        final Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this,animId);

        // set animation listener
        animation.setAnimationListener(animationListener);

        btn.startAnimation(animation);
    }

    private void handleImageAnimationXml(ImageView img, int animId)
    {
        // HandleClickAnimationXML
        // Load the Animation
        final Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this,animId);

        // set animation listener
        animation.setAnimationListener(animationListener);

        img.startAnimation(animation);
    }
}