package edu.rosehulman.rosefire;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import edu.rosehulman.gearlocker.R;

import static edu.rosehulman.gearlocker.R.color.colorBlue;

public final class WebLoginActivity extends Activity {

    private WebView mLoginScreen;

    public static final String REGISTRY_TOKEN = "registry";
    public static final String JWT_TOKEN = "token";
    public static final String ERROR = "error";

    public final String javascriptInjection = "document.body.style.backgroundColor=\"#94A899\";\n" +
            "document.getElementById(\"submit\").style.backgroundColor=\"#CEBF8A\";\n" +
            "document.getElementById(\"submit\").style.color=\"black\";\n" +
            "document.getElementById(\"submit\").style.borderColor=\"#CEBF8A\";\n" +
            "document.getElementById(\"password\").style.backgroundColor=\"#C4C4C4\";\n" +
            "document.getElementById(\"username\").style.backgroundColor=\"#C4C4C4\";";


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginScreen = new WebView(this);
        this.setContentView(mLoginScreen);
        String token = getIntent().getStringExtra(REGISTRY_TOKEN);
        try {
            mLoginScreen.loadUrl("https://rosefire.csse.rose-hulman.edu/webview/login?registryToken=" + URLEncoder.encode(token, "UTF-8") + "&platform=android");
        } catch (UnsupportedEncodingException e) {
            // TODO: Also catch loading errors
            onLoginFail("Invalid registryToken");
        }
        WebSettings webSettings = mLoginScreen.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mLoginScreen.addJavascriptInterface(this, "Android");
        mLoginScreen.loadUrl("javascript:" + javascriptInjection);
    }


    private void onLoginSuccess(String token) {
        if (Rosefire.DEBUG) Log.d("RFA", token);
        Intent data = new Intent();
        data.putExtra(JWT_TOKEN, token);
        setResult(RESULT_OK, data);
        finish();
    }

    private void onLoginFail(String message) {
        if (Rosefire.DEBUG) Log.d("RFA", message);
        Intent data = new Intent();
        data.putExtra(ERROR, message);
        setResult(RESULT_CANCELED, data);
        finish();
    }

    @JavascriptInterface
    public void finish(String token) {
        onLoginSuccess(token);
    }

}
