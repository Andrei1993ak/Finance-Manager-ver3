package com.gmail.a93ak.andrei19.finance30.util.UniversalLoader;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

abstract class MemoryCache<MyObj> {

    private Map<String, MyObj> cache = Collections.synchronizedMap(new LinkedHashMap<String, MyObj>(10, 1.5f, true));
    private long size = 0;
    private long limit = Runtime.getRuntime().maxMemory() / 4;

    public MemoryCache() {

    }

    abstract int getSize(MyObj myObj);

    MyObj getFromMemoryCache(String id) {
        try {
            if (!cache.containsKey(id))
                return null;
            return cache.get(id);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    void putToMemoryCache(String id, MyObj myObj) {
        try {
            if (cache.containsKey(id)) {
                size -= getSizeInBytes(cache.get(id));
            }
            cache.put(id, myObj);
            size += getSizeInBytes(myObj);
            checkSize();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void checkSize() {
        if (size > limit) {
            Iterator<Entry<String, MyObj>> iterator = cache.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, MyObj> entry = iterator.next();
                size -= getSizeInBytes(entry.getValue());
                iterator.remove();
                if (size <= limit)
                    break;
            }
        }
    }

   void clear() {
        try {
            cache.clear();
            size = 0;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private long getSizeInBytes(MyObj myObj) {
        if (myObj == null)
            return 0;
        return getSize(myObj);
    }
}