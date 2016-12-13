package com.github.andrei1993ak.finances.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.andrei1993ak.finances.api.UpdateCurrenciesJob;
import com.github.andrei1993ak.finances.model.TableQueryGenerator;
import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.util.Constants;

public class ZeroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final long lastTimeUpdate = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).getLong(Constants.LAST_TIME_UPDATE, -1L);
        if (System.currentTimeMillis() - lastTimeUpdate > Constants.ONE_DAY_IN_MILLIS) {
            new UpdateCurrenciesJob().execute();
        }
        redirect();
    }

    private void redirect() {
        if (hasPinCode()) {
            startActivity(new Intent(this, PinEntryActivity.class));
        } else {
            startActivity(new Intent(this, StartingActivity.class));
        }

        finish();
    }

    private boolean hasPinCode() {
        return getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE).getBoolean(Constants.HAS_PIN, false);
    }
}
