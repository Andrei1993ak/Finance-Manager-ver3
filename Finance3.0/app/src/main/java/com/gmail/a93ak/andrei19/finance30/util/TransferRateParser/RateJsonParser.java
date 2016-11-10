package com.gmail.a93ak.andrei19.finance30.util.transferRateParser;

import android.os.AsyncTask;

import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperCurrency;
import com.gmail.a93ak.andrei19.finance30.util.MyHttpClient;

import org.json.JSONObject;

public class RateJsonParser extends AsyncTask<Long, Void, Double> {

    private static final String URL = "http://floatrates.com/daily/";
    private static final String RATE = "rate";
    private static final String JSON = ".json";

    private final OnParseCompleted listener;

    public RateJsonParser(final OnParseCompleted view) {
        listener = view;
    }

    @Override
    protected Double doInBackground(final Long... params) {
        if (params[0].equals(params[1])) {
            return 1.0;
        } else {
            final String codeFrom = DBHelperCurrency.getInstance().get(params[0]).getCode();
            final String codeTo = DBHelperCurrency.getInstance().get(params[1]).getCode();
            try {
                final String jsonString = new MyHttpClient().get(URL + codeFrom + JSON);
                final JSONObject root = new JSONObject(jsonString);
                final JSONObject inner = root.getJSONObject(codeTo.toLowerCase());
                return inner.getDouble(RATE);
            } catch (final Exception e) {
                return -1.0;
            }
        }
    }

    @Override
    protected void onPostExecute(final Double result) {
        listener.onParseCompleted(result);
    }
}
