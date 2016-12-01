package com.github.andrei1993ak.finances.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.andre.myapplication.backend.myApi.MyApi;
import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrencyOfficial;
import com.github.andrei1993ak.finances.model.models.CurrencyOfficial;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.util.ContextHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class UpdateCurrenciesJob extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(final Void... params) {
        final MyApi.GetCurrencies getCurrenciesJob;
        String response;
        try {
            getCurrenciesJob = ApiManager.get().myApi().getCurrencies();
            response = getCurrenciesJob.execute().getData();
        } catch (final IOException e) {
            response = null;
        }
        if (response != null) {
            try {
                final JSONObject root = new JSONObject(response);
                final Iterator<String> keys = root.keys();
                final DBHelperCurrencyOfficial dbHelper = (DBHelperCurrencyOfficial) ((App) ContextHolder.getInstance().getContext()).getDbHelper(CurrencyOfficial.class);
                while (keys.hasNext()) {
                    final JSONObject inner = root.getJSONObject(keys.next());
                    final CurrencyOfficial fromWeb = new CurrencyOfficial(inner.getString("code"), inner.getString("name"));
                    final CurrencyOfficial fromDB = dbHelper.getByCode(fromWeb.getCode());
                    if (fromDB == null) {
                        dbHelper.add(fromWeb);
                    } else {
                        if (!fromDB.getName().equals(fromWeb.getName())) {
                            fromDB.setName(fromWeb.getName());
                            dbHelper.update(fromDB);
                        }
                    }
                }
                final SharedPreferences prefs = ContextHolder.getInstance().getContext().getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(Constants.LAST_TIME_UPDATE, System.currentTimeMillis());
                editor.apply();
            } catch (final JSONException e) {
                Log.e("FamilyFinances", "error parsing new currencies");
            }
        }
        return null;
    }
}
