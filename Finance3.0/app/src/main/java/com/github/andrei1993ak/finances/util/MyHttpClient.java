package com.github.andrei1993ak.finances.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyHttpClient {

    public String get(final String url) {
        final String response;
        try {
            final URL reqUrl = new URL(url);
            final HttpURLConnection connection = (HttpURLConnection) reqUrl.openConnection();
            connection.setRequestMethod("GET");
            final InputStream inputStream = connection.getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            final StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            response = builder.toString();
            inputStream.close();
        } catch (final IOException e) {
            return null;
        }
        return response;
    }

}
