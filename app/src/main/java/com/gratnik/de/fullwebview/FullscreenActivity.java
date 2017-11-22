package com.gratnik.de.fullwebview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {

    private boolean waitForUrl = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_fullscreen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        loadUrl();


    }


    @Override
    protected void onResume() {
        super.onResume();
        setFull();
    }

    protected void setFull() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else
        {
            View decorView = getWindow().getDecorView();
            //Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void loadUrl() {
        WebView myWebView = findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.url);
        String url = sharedPref.getString(getString(R.string.url), defaultValue);
        myWebView.loadUrl(url);
        setFull();

    }

    @Override
    public void onBackPressed() {
        //show input dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(FullscreenActivity.this);
        builder.setTitle("Input url");

// Set up the input
        final EditText input = new EditText(FullscreenActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.url);
        String url = sharedPref.getString(getString(R.string.url), defaultValue);

        input.setText(url);

        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.url), input.getText().toString());
                editor.commit();
                loadUrl();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                loadUrl();
            }
        });

        builder.show();
    }

}
