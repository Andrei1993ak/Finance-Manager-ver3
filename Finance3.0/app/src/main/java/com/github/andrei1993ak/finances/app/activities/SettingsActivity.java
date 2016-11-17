package com.github.andrei1993ak.finances.app.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.util.Constants;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        if (getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).getBoolean(Constants.THEME, false)) {
            setTheme(R.style.Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setTitle(R.string.settings);
        final Switch themeSwitch = (Switch) findViewById(R.id.themeSwitch);
        if (getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).getBoolean(Constants.THEME, false)) {
            themeSwitch.setChecked(true);
        }
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                final SharedPreferences prefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = prefs.edit();
                if (isChecked) {
                    editor.putBoolean(Constants.THEME, true);
                    editor.apply();
                    recreate();
                } else {
                    editor.putBoolean(Constants.THEME, false);
                    editor.apply();
                    recreate();
                }
            }
        });
    }
}
