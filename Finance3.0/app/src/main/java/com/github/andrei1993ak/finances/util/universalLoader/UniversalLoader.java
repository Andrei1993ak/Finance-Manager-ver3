package com.github.andrei1993ak.finances.util.universalLoader;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class UniversalLoader<MyObj, Destination> implements IUniversalLoader<MyObj,Destination> {

    private static final int CONNECTION_TIMEOUT = 3000;
    private static final int READ_TIMEOUT = 3000;
    private static final int PRE_SIZE = 400;

    private final int connectTimeout;
    private final int readTimeout;
    private final int maxPreSize;
    private final Context context;
    private final FileCache fileCache;
    private final Map<Destination, String> views = Collections.synchronizedMap(new WeakHashMap<Destination, String>());
    private final ExecutorService executorService;
    private final MemoryCache<MyObj> memoryCache;

    public UniversalLoader(final Context context) {
        this.connectTimeout = CONNECTION_TIMEOUT;
        this.readTimeout = READ_TIMEOUT;
        this.maxPreSize = PRE_SIZE;
        this.context = context;
        this.fileCache = FileCache.getInstance(context);
        this.memoryCache = new MemoryCache<MyObj>() {
            @Override
            int getSize(final MyObj myObj) {
                return getSizeObj(myObj);
            }
        };
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public UniversalLoader(final Context context, final int connectTimeout, final int readTimeout, final int threadsCount, final int maxPreSize) {
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.maxPreSize = maxPreSize;
        this.context = context;
        this.fileCache = FileCache.getInstance(context);
        this.memoryCache = new MemoryCache<MyObj>() {
            @Override
            int getSize(final MyObj myObj) {
                return getSizeObj(myObj);
            }
        };
        this.executorService = Executors.newFixedThreadPool(threadsCount);
    }

    public void clearCashes(final String url) {
        memoryCache.clear(url);
        fileCache.clear(url);
    }

    public void load(final String url, final Destination destination) {
        views.put(destination, url);
        final MyObj myObj = memoryCache.getFromMemoryCache(url);
        if (myObj != null) {
            set(myObj, destination);
        } else {
            final Comparison comparison = new Comparison(url, destination);
            executorService.submit(new Loader(comparison));
            set(null, destination);
        }
    }

    @Nullable
    private MyObj getMyObjFromFileOrUrl(final String url) {
        final File file = fileCache.getFile(url);
        if (file == null) {
            return null;
        }
        MyObj myObj = decodeFromFile(file, maxPreSize);
        if (myObj != null) {
            return myObj;
        } else {
            InputStream is = null;
            OutputStream os = null;
            try {
                final URL imageUrl = new URL(url);
                final URLConnection conn = imageUrl.openConnection();
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
                is = conn.getInputStream();
                os = new FileOutputStream(file);
                copyStream(is, os);
                myObj = decodeFromFile(file, maxPreSize);
                return myObj;
            } catch (final Throwable ex) {
                if (ex instanceof OutOfMemoryError)
                    memoryCache.clear();
                return null;
            } finally {
                try {
                    assert is != null;
                    is.close();
                    assert os != null;
                    os.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void copyStream(final InputStream is, final OutputStream os) throws IOException {
        final int buffer_size = 1024;
        final byte[] bytes = new byte[buffer_size];
        int count;
        for (; ; ) {
            count = is.read(bytes, 0, buffer_size);
            if (count == -1)
                break;
            os.write(bytes, 0, count);
        }
    }

    private boolean isReused(final Comparison comparison) {
        final String tag = views.get(comparison.destination);
        return tag == null || !tag.equals(comparison.url);
    }

    private class Comparison {

        private final String url;
        private final Destination destination;

        Comparison(final String url, final Destination destination) {
            this.url = url;
            this.destination = destination;
        }
    }

    private class Loader implements Runnable {

        private final Comparison comparison;

        Loader(final Comparison comparison) {
            this.comparison = comparison;
        }

        @Override
        public void run() {
            if (isReused(comparison))
                return;
            final MyObj myObj = getMyObjFromFileOrUrl(comparison.url);
            memoryCache.putToMemoryCache(comparison.url, myObj);
            if (isReused(comparison))
                return;
            final Activity a = (Activity) UniversalLoader.this.context;
            final UIWorker runnable = new UIWorker(myObj, comparison);
            a.runOnUiThread(runnable);

        }
    }

    private class UIWorker implements Runnable {
        private final MyObj myObj;
        private final Comparison comparison;

        private UIWorker(final MyObj myObj, final Comparison comparison) {
            this.myObj = myObj;
            this.comparison = comparison;
        }

        public void run() {
            if (isReused(comparison)) {
                return;
            }
            if (myObj != null) {
                set(myObj, comparison.destination);
            } else {
                set(null, comparison.destination);
            }
        }
    }

}
