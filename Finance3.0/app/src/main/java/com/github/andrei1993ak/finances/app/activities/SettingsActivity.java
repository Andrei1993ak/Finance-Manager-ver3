package com.github.andrei1993ak.finances.app.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.BaseActivity;
import com.github.andrei1993ak.finances.app.SetPinActivity;
import com.github.andrei1993ak.finances.model.backupUtil.DBBackupUtils;
import com.github.andrei1993ak.finances.model.backupUtil.OnBackupOperationCompleted;
import com.github.andrei1993ak.finances.notification.AlarmReceiver;
import com.github.andrei1993ak.finances.util.Constants;

import java.util.Calendar;

public class SettingsActivity extends BaseActivity implements OnBackupOperationCompleted {

    private Switch pinSwitch;
    private Switch autoBackupSwitch;
    private Switch notificationSwitch;
    private Switch themeSwitch;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setTitle(R.string.settings);

        initFields();
    }

    private void initFields() {

        initThemes();
        initNotification(this);
        initPin();
        initAutoBackup();

        final Button buttonOnlineBackup = (Button) findViewById(R.id.button_online_backup);
        buttonOnlineBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new DBBackupUtils().backupToCloud(SettingsActivity.this);

            }
        });

        final Button buttonOnlineRestore = (Button) findViewById(R.id.button_online_restore);
        buttonOnlineRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new DBBackupUtils().restoreFromCloud(SettingsActivity.this);
            }
        });

        final Button buttonBackup = (Button) findViewById(R.id.button_backup);
        buttonBackup.setOnClickListener(new View.OnClickListener() {
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

    }

    private void initAutoBackup() {
        autoBackupSwitch = (Switch) findViewById(R.id.autoBackupSwitch);
        if (getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).getBoolean(Constants.AUTO_BACKUP_ENABLED, false)) {
            autoBackupSwitch.setChecked(true);
        }
        autoBackupSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                final SharedPreferences prefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = prefs.edit();
                if (isChecked) {
                    editor.putBoolean(Constants.AUTO_BACKUP_ENABLED, true);
                } else {
                    editor.putBoolean(Constants.AUTO_BACKUP_ENABLED, false);
                }
                editor.apply();
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        final SharedPreferences prefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        if (resultCode == RESULT_OK) {
            pinSwitch.setChecked(true);
            editor.putBoolean(Constants.HAS_PIN, true);
            editor.apply();
        } else {
            pinSwitch.setChecked(false);
            editor.putBoolean(Constants.HAS_PIN, false);
            editor.apply();
        }
    }

    private void initNotification(final Context context) {
        notificationSwitch = (Switch) findViewById(R.id.notificationSwitch);
        if (getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).getBoolean(Constants.NOTIFICATION, false)) {
            notificationSwitch.setChecked(true);
        }
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                final SharedPreferences prefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = prefs.edit();
                final Intent intent = new Intent(context, AlarmReceiver.class);
                final PendingIntent alarmSender = PendingIntent.getBroadcast(context, 0, intent, 0);
                final AlarmManager alarmManager = (AlarmManager) SettingsActivity.this.getSystemService(ALARM_SERVICE);
                if (!isChecked) {
                    alarmManager.cancel(alarmSender);
                    editor.putBoolean(Constants.NOTIFICATION, false);
                    editor.apply();
                } else {
                    if (!getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).getBoolean(Constants.NOTIFICATION, false)) {
                        notificationSwitch.setChecked(false);
                        new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(final TimePicker timePicker, final int hourOfDay, final int minutes) {
                                final Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minutes);
                                c.set(Calendar.SECOND, 0);
                                final long firsTime = c.getTimeInMillis();
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firsTime, 60L * 60L * 1000L, alarmSender);
                                editor.putBoolean(Constants.NOTIFICATION, true);
                                editor.apply();
                                notificationSwitch.setChecked(true);
                            }
                        }, 0, 0, true).show();
                    }
                }
            }
        });
    }

    private void initThemes() {
        themeSwitch = (Switch) findViewById(R.id.themeSwitch);
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
                    setResult(RESULT_OK);
                } else {
                    editor.putBoolean(Constants.THEME, false);
                    editor.apply();
                    recreate();
                    setResult(RESULT_OK);
                }
            }
        });

    }

    private void initPin() {
        pinSwitch = (Switch) findViewById(R.id.pinSwitch);
        if (getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).getBoolean(Constants.HAS_PIN, false)) {
            pinSwitch.setChecked(true);
        }
        pinSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final SharedPreferences prefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = prefs.edit();
                final Switch pinSwitch = (Switch) view;
                if (!pinSwitch.isChecked()) {
                    editor.putBoolean(Constants.HAS_PIN, false);
                    editor.apply();
                } else {
                    startActivityForResult(new Intent(SettingsActivity.this, SetPinActivity.class), 10);
                }
            }
        });
    }

    @Override
    public void onBackupCompleted(final Boolean b) {
        if (b) {
            Toast.makeText(this, "ok", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "false", Toast.LENGTH_LONG).show();
        }
    }
}
