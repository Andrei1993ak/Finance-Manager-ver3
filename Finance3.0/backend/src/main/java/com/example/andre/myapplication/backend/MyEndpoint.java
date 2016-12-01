/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.andre.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.repackaged.com.google.api.client.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Named;


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

    private static final int READ_TIMEOUT_MILLIS = 10000;
    private static final int CONNECTION_TIMEOUT_MILLIS = 15000;

    @ApiMethod(name = "getCurrencies")
    public MyBean getCurrencies() {
        final MyBean response = new MyBean();
        final HttpURLConnection conn;
        InputStream inputStream = null;
        try {
            final URL url = new URL("http://www.floatrates.com/daily/usd.json");
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT_MILLIS /* milliseconds */);
            conn.setConnectTimeout(CONNECTION_TIMEOUT_MILLIS /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            inputStream = conn.getInputStream();
            response.setData(convertStreamToString(inputStream));
            return response;
        } catch (final IOException e) {
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (final IOException e) {

            }
        }
    }

    private static String convertStreamToString(final java.io.InputStream is) {
        java.util.Scanner scanner = new java.util.Scanner(is).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}
