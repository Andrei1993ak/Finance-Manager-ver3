package com.github.andrei1993ak.finances.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.util.Constants;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).getBoolean(Constants.THEME, false)) {
            setTheme(R.style.Dark);
        }
    }
}
