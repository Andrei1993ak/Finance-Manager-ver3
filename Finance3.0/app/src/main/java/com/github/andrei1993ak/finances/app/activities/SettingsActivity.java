package com.github.andrei1993ak.finances.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.backupUtil.DBBackupUtils;
import com.github.andrei1993ak.finances.signinByAppEngine.IdTokenActivity;
import com.github.andrei1993ak.finances.signinByAppEngine.ServerAuthCodeActivity;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.signinByAppEngine.SignInActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setTitle(R.string.settings);
        initFields();
    }

    private void initFields() {
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

        final Button button = (Button) findViewById(R.id.button_backup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new DBBackupUtils().backupDB();
            }
        });

        final Button buttonRestore = (Button) findViewById(R.id.button_restore);
        buttonRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new DBBackupUtils().restoreDB();
            }
        });

        final Button buttonSignin = (Button) findViewById(R.id.button_sign_in);
        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startActivity(new Intent(SettingsActivity.this,SignInActivity.class));
            }
        });

        final Button buttonToken = (Button) findViewById(R.id.button_token_id);
        buttonToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startActivity(new Intent(SettingsActivity.this,IdTokenActivity.class));
            }
        });

        final Button buttonServerAuthCode = (Button) findViewById(R.id.button_server_auth_code);
        buttonServerAuthCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startActivity(new Intent(SettingsActivity.this,ServerAuthCodeActivity.class));
            }
        });
    }
}
