package com.github.andrei1993ak.finances.util.universalLoader;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TODO fix all warnings
//TODO create base interface first, then extends to abstract class
public abstract class UniversalLoader<MyObj, Destination> {

    public abstract int getSizeObj(MyObj myObj);

    public abstract MyObj decodeFromFile(File file, int PreSize);

    public abstract void set(MyObj myObj, Destination destination);

    private int connectTimeout;
    private int readTimeout;
    private int maxPreSize;

    private Context context;

    private FileCache fileCache;
    //TODO why its called like ImageViews?
    private Map<Destination, String> imageViews = Collections.synchronizedMap(new WeakHashMap<Destination, String>());
    private ExecutorService executorService;
    private MemoryCache<MyObj> memoryCache;

    public UniversalLoader(Context context) {
        //TODO move to constants
        //TODO add this to all members
        connectTimeout=3000;
        readTimeout = 3000;
        maxPreSize = 400;
        this.context = context;
        fileCache = FileCache.getInstance(context);
        memoryCache = new MemoryCache<MyObj>() {
            @Override
            int getSize(MyObj myObj) {
                return getSizeObj(myObj);
            }
        };
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public UniversalLoader(Context context, int connectTimeout, int readTimeout, int threadsCount, int maxPreSize) {
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.maxPreSize = maxPreSize;
        this.context = context;
        fileCache = FileCache.getInstance(context);
        memoryCache = new MemoryCache<MyObj>() {
            @Override
            int getSize(MyObj myObj) {
                return getSizeObj(myObj);
            }
        };
        executorService = Executors.newFixedThreadPool(threadsCount);
    }

    public void clearCashes(String url){
        memoryCache.clear(url);
        fileCache.clear(url);
    }

    public void load(String url, Destination destination) {
        imageViews.put(destination, url);
        MyObj myObj = memoryCache.getFromMemoryCache(url);
        if (myObj != null) {
            set(myObj, destination);
        } else {
            Comparison comparison = new Comparison(url, destination);
            executorService.submit(new Loader(comparison));
            set(null, destination);
        }
    }

    private MyObj getMyObjFromFileOrUrl(String url) {
        File file = fileCache.getFile(url);
        MyObj myObj = decodeFromFile(file, maxPreSize);
        if (myObj != null) {
            return myObj;
        }
        try {
            URL imageUrl = new URL(url);
            URLConnection conn = imageUrl.openConnection();
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(file);
            copyStream(is, os);
            //TODO need to close in final block
            is.close();
            os.close();
            myObj = decodeFromFile(file, maxPreSize);
            return myObj;
        } catch (Throwable ex) {
            //TODO why it ignored?
            //TODO add callbacks
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    private void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                //TODO move count from circle
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            //TODO WTF? we can't ignore exceptions
            ex.printStackTrace();
        }
    }

    private boolean isReused(Comparison comparison) {
        String tag = imageViews.get(comparison.destination);
        return tag == null || !tag.equals(comparison.url);
    }

    private class Comparison {

        private String url;
        private Destination destination;

        Comparison(String url, Destination destination) {
            this.url = url;
            this.destination = destination;
        }
    }

    private class Loader implements Runnable {

        private Comparison comparison;

        Loader(Comparison comparison) {
            this.comparison = comparison;
        }

        @Override
        public void run() {
            if (isReused(comparison))
                return;
            MyObj myObj = getMyObjFromFileOrUrl(comparison.url);
            memoryCache.putToMemoryCache(comparison.url, myObj);
            if (isReused(comparison))
                return;
            Activity a = (Activity) UniversalLoader.this.context;
            UIWorker runnable = new UIWorker(myObj, comparison);
            a.runOnUiThread(runnable);

        }
    }

    private class UIWorker implements Runnable {
        private MyObj myObj;
        private Comparison comparison;

        private UIWorker(MyObj myObj, Comparison comparison) {
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
