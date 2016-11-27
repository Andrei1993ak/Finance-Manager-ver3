package com.github.andrei1993ak.finances.util;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MyHttpClient {

    @Nullable
    public String get(final String url) {
        final String response;
        InputStream inputStream = null;

        try {
            final URL reqUrl = new URL(url);
            final HttpURLConnection connection = (HttpURLConnection) reqUrl.openConnection();
            connection.setRequestMethod("GET");
            inputStream = connection.getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            final StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            response = builder.toString();

            return response;
        } catch (final IOException e) {
            return null;
        } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

}
