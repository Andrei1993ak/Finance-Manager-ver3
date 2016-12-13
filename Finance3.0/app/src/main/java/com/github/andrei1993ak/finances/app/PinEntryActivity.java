package com.github.andrei1993ak.finances.app;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.util.Constants;

public class PinEntryActivity extends BaseActivity {

    private String userEntered;
    private boolean keyPadLockedFlag = false;
    private TextView statusView;
    private TextView passwordInput;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        setContentView(R.layout.activity_pin);
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.pinLayout);
        final Drawable wallpaperDrawable = WallpaperManager.getInstance(this).getDrawable();
        layout.setBackground(wallpaperDrawable);
        userEntered = "";
        passwordInput = (TextView) findViewById(R.id.passwordTextView);
        statusView = (TextView) findViewById(R.id.statusview);
        statusView.setText(R.string.enter_pin);
        final Button button0 = (Button) findViewById(R.id.button0);
        button0.setOnClickListener(pinButtonHandler);

        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(pinButtonHandler);

        final Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(pinButtonHandler);

        final Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(pinButtonHandler);

        final Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(pinButtonHandler);

        final Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(pinButtonHandler);

        final Button button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(pinButtonHandler);

        final Button button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(pinButtonHandler);

        final Button button8 = (Button) findViewById(R.id.button8);
        button8.setOnClickListener(pinButtonHandler);

        final Button button9 = (Button) findViewById(R.id.button9);
        button9.setOnClickListener(pinButtonHandler);

        final Button buttonDelete = (Button) findViewById(R.id.buttonDeleteBack);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (keyPadLockedFlag) {
                    return;
                }

                if (userEntered.length() > 0) {
                    userEntered = "";
                    passwordInput.setText("");
                }
            }
        });

    }

    final View.OnClickListener pinButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            if (keyPadLockedFlag) {
                return;
            }

            final Button pressedButton = (Button) v;

            final int PIN_LENGTH = 4;
            userEntered = userEntered + pressedButton.getText();
            final String passInputText = passwordInput.getText().toString() + Constants.COMMA;
            passwordInput.setText(passInputText);

            if (userEntered.length() == PIN_LENGTH) {
                final String userPin = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).getString(Constants.PIN, null);

                if (userEntered.equals(userPin)) {
                    statusView.setTextColor(Color.GREEN);

                    finish();
                    startActivity(new Intent(PinEntryActivity.this, StartingActivity.class));
                } else {
                    statusView.setTextColor(Color.RED);
                    statusView.setText(R.string.wrong_pin);
                    keyPadLockedFlag = true;

                    new LockKeyPadOperation().execute();
                }
            }
        }
    };

    private class LockKeyPadOperation extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(final Void... params) {
            try {
                Thread.sleep(DateUtils.SECOND_IN_MILLIS * 2);
            } catch (final InterruptedException e) {
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            statusView.setText("");
            passwordInput.setText("");
            userEntered = "";
            keyPadLockedFlag = false;
        }

    }
}