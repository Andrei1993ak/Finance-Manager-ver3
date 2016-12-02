package com.github.andrei1993ak.finances.util.transferRateParser;

import android.os.AsyncTask;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrency;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.util.ContextHolder;
import com.github.andrei1993ak.finances.util.MyHttpClient;

import org.json.JSONObject;

public class RateJsonParser extends AsyncTask<Long, Void, Double> {

    private static final String URL = "http://floatrates.com/daily/";
    private static final String RATE = "rate";
    private static final String JSON = ".json";

    private final IOnParseCompleted listener;
    private final DBHelperCurrency dbHelperCurrency;

    public RateJsonParser(final IOnParseCompleted view) {
        listener = view;
        dbHelperCurrency = ((DBHelperCurrency) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Currency.class));
    }

    @Override
    protected Double doInBackground(final Long... params) {
        if (params[0].equals(params[1])) {
            return 1.0;
        } else {
            final String codeFrom = dbHelperCurrency.get(params[0]).getCode();
            final String codeTo = dbHelperCurrency.get(params[1]).getCode();
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
