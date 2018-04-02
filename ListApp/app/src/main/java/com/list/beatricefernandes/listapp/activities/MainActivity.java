package com.list.beatricefernandes.listapp.activities;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.list.beatricefernandes.listapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;


public class MainActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.activity_main_text_view) protected AutoCompleteTextView mTextViewURL;



    //region --- Lifecycle Methods ---
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeViews();

    }

    @Optional
    @OnClick({R.id.activity_main_button})
    protected void onClick() {

        this.callWebViewActivity();

    }

    private void initializeViews() {

        ArrayList<String> urlsArray = new ArrayList<>();
        //optional add an url here
        urlsArray.add("google.com/urlexamplemylist");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, urlsArray);

        if (this.mTextViewURL != null) {
            this.mTextViewURL.setThreshold(1);
            this.mTextViewURL.setAdapter(adapter);
        }

        this.mTextViewURL.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    callWebViewActivity();
                }
                return false;
            }
        });

    }

    private void callWebViewActivity() {

        Intent intent = new Intent(this, TheWebView.class);
        if (this.mTextViewURL != null) {
            intent.putExtra("url", this.mTextViewURL.getText().toString());
        }
        startActivity(intent);
    }


}
