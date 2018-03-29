package com.list.beatricefernandes.listapp.activities;


import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import com.list.beatricefernandes.listapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.activity_main_text_view) protected AutoCompleteTextView mTextViewURL;

    @Nullable
    @BindView(R.id.activity_main_button) protected Button mButtonOk;


    //region --- Lifecycle Methods ---
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



    }



}
