package com.github.andrei1993ak.finances.api;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.andre.myapplication.backend.myApi.MyApi;

import java.io.IOException;

public class UpdateCurrenciesJob extends AsyncTask<Context, Void, String> {

    private Context context;

    @Override
    protected String doInBackground(final Context... params) {
        final MyApi.GetCurrencies getCurrenciesJob;
        String response;
        context = params[0];
        try {
            getCurrenciesJob = ApiManager.get().myApi().getCurrencies();
            response = getCurrenciesJob.execute().getData();
        } catch (final IOException e) {
            response = null;
        }
        return response;
    }

    @Override
    protected void onPostExecute(final String s) {
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }

}
