package com.gmail.a93ak.andrei19.finance30.api;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.andre.myapplication.backend.myApi.MyApi;

import java.io.IOException;

public class HelloEndpoint extends AsyncTask<Context, Void, String> {

    private Context context;

    @Override
    protected String doInBackground(Context... params) {
        MyApi.SayHi sayHi;
        String response;
        context = params[0];
        try {
            sayHi = ApiManager.get().myApi().sayHi("Andrei");
            response = sayHi.execute().getData();
        } catch (IOException e) {
            e.printStackTrace();
            response = ":(";
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context,s,Toast.LENGTH_LONG).show();
    }

}
