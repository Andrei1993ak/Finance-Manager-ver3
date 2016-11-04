package com.gmail.a93ak.andrei19.finance30.util.TransferRateParser;

import android.os.AsyncTask;

import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperCurrency;
import com.gmail.a93ak.andrei19.finance30.util.ContextHolder;
import com.gmail.a93ak.andrei19.finance30.util.MyHttpClient;

import org.json.JSONObject;

public class RateJsonParser extends AsyncTask<Long, Void, Double> {

    private static final String URL = "http://floatrates.com/daily/";

    private OnParseCompleted listener;

    public RateJsonParser(OnParseCompleted view) {
        listener = view;
    }

    @Override
    protected Double doInBackground(Long... params) {
        if (params[0].equals(params[1])) {
            return 1.0;
        } else {
            final String codeFrom = DBHelperCurrency.getInstance(DBHelper.getInstance(ContextHolder.getInstance().getContext())).get(params[0]).getCode();
            final String codeTo = DBHelperCurrency.getInstance(DBHelper.getInstance(ContextHolder.getInstance().getContext())).get(params[1]).getCode();
            try {
                final String jsonString = new MyHttpClient().get(URL + codeFrom + ".json");
                final JSONObject root = new JSONObject(jsonString);
                final JSONObject inner = root.getJSONObject(codeTo.toLowerCase());
                return inner.getDouble("rate");
            } catch (Exception e) {
                return -1.0;
            }
        }
    }

    @Override
    protected void onPostExecute(Double result) {
        listener.onParseCompleted(result);
    }
}
