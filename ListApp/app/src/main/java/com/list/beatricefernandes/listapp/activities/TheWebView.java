package com.list.beatricefernandes.listapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.list.beatricefernandes.listapp.BuildConfig;
import com.list.beatricefernandes.listapp.R;
import com.list.beatricefernandes.listapp.helpers.NetworkHelper;
import com.list.beatricefernandes.listapp.services.ServiceLocation;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/** Created by beatrice.fernandes on 31/03/18.
 */

public class TheWebView extends Activity {


        //region --- Constants ---
        //insert the callbacks here
        private static final String LOGS_URL = "callback=";
        private static final String BARCODE_URL = "callback=";
        private static final String LOCATION_URL = "callback=";
        private static final int REQUEST_CODE_BARCODE = 1;
        //endregion

        //region --- Variables ---
        private String jsFunction, url;
        private boolean mReadyToLeave;
        //endregion

        @Nullable
        @BindView(R.id.activity_web_view)
        protected WebView mWebView;

        //region --- Lifecycle Methods ---
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_the_web_view);
            ButterKnife.bind(this);

            Bundle bundle = getIntent().getExtras();

            assert bundle != null;
            if (bundle.getString("url") != null) {

                this.url = bundle.getString("url");

            } else {
                //insert URL
                this.url = "url";

            }

            initializeViews();

        }

        @Override
        protected void onStart() {
            super.onStart();

            startService(new Intent(this, ServiceLocation.class));
        }

        @Override
        public void onBackPressed() {

            if (mReadyToLeave)
                super.onBackPressed();
            else {
                Toast.makeText(this, "Press again to leave app", Toast.LENGTH_LONG).show();
                mReadyToLeave = true;

                Handler h = new Handler();
                h.postDelayed(new Runnable(){
                    public void run(){
                        mReadyToLeave = false;
                    }
                }, 3000);
            }
        }
        //endregion

        private String getDeviceParameters() {

            String language = Locale.getDefault().toString();
            String versionCode = String.valueOf(BuildConfig.VERSION_CODE);
            String versionName = BuildConfig.VERSION_NAME;
            int sdkVersion = Build.VERSION.SDK_INT;
            String connectivity = NetworkHelper.getConnectivityType(this);

            return "Language: " + language + ", " +
                    "Version Code: " + versionCode + ", " +
                    "Version Name: " + versionName + ", " +
                    "SDK Version: " + sdkVersion + ", " +
                    "Connectivity Type: " + connectivity;
        }

        private void initializeViews() {

            if(NetworkHelper.isInternetConnectionON(this)) {

                this.mWebView.getSettings().setJavaScriptEnabled(true);
                this.mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                this.mWebView.getSettings().setUserAgentString("MyList"+this.mWebView.getSettings().getUserAgentString()+"/mylist");
                this.mWebView.setWebChromeClient(new WebChromeClient());
                this.mWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {

                        if (url.contains(LOGS_URL)) {

                            jsFunction = "yourParamiters";
                            String javaScript = "javascript:" + jsFunction + "(\"" + getDeviceParameters() + "\");";
                            mWebView.loadUrl(javaScript);
                        } else if(url.contains(BARCODE_URL)) {

                            int equalsIndex = url.indexOf("=");

                            if (equalsIndex != -1) {
                                jsFunction = url.substring(equalsIndex + 1, url.length());
                                openBarcodeReader();
                            }
                        } else if(url.contains(LOCATION_URL)) {

                            jsFunction = "yourParamiter";
                            SharedPreferences sharedPreferences = getSharedPreferences("Location", MODE_PRIVATE);
                            String latitude = sharedPreferences.getString("Latitude", null);
                            String longitude = sharedPreferences.getString("Longitude", null);
                            String location = "javascript:" + jsFunction + "(true, ['" + latitude + "','" + longitude + "'])";
                            mWebView.loadUrl(location);

                        } else {
                            view.loadUrl(url);
                        }
                        return true;
                    }
                });

                mWebView.loadUrl(this.url);
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE_BARCODE) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    Toast.makeText(getApplicationContext(), "Barcode: " + barcode.displayValue, Toast.LENGTH_LONG).show();
                    jsFunction = "yourParamiters";
                    String javaScript = "javascript:" + jsFunction + "(true, " + barcode.displayValue + ");";
                    if (mWebView != null) {
                        mWebView.loadUrl(javaScript);
                    }
                }
            }
        }

        public void openBarcodeReader() {

            Intent intent = new Intent(this, TheBarcode.class);
            startActivityForResult(intent, REQUEST_CODE_BARCODE);
        }


    }



