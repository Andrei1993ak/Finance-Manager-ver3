package com.example.andre.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.andre.example.com",
                ownerName = "backend.myapplication.andre.example.com",
                packagePath = ""
        )
)
public class MyEndpoint {

    private String responseString;
    private long lastUpdateTime = 0;

    private static final int READ_TIMEOUT_MILLIS = 10000;
    private static final int CONNECTION_TIMEOUT_MILLIS = 15000;

    private final long ONE_DAY_IN_MILLIS = 86400000;

    @ApiMethod(name = "getCurrencies")
    public MyBean getCurrencies() {

        final MyBean response = new MyBean();
        if (System.currentTimeMillis() - lastUpdateTime < ONE_DAY_IN_MILLIS) {
            response.setData(responseString);
            return response;
        }

        final HttpURLConnection conn;
        InputStream inputStream = null;
        try {
            final URL url = new URL("http://www.floatrates.com/daily/usd.json");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT_MILLIS);
            conn.setConnectTimeout(CONNECTION_TIMEOUT_MILLIS);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            inputStream = conn.getInputStream();
            responseString = convertStreamToString(inputStream);
            lastUpdateTime = System.currentTimeMillis();
            response.setData(responseString);
            return response;
        } catch (final IOException e) {
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String convertStreamToString(final java.io.InputStream is) {
        final java.util.Scanner scanner = new java.util.Scanner(is).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}
