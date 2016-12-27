package com.github.andrei1993ak.finances.util.universalLoader;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

abstract class MemoryCache<MyObj> {

    private final Map<String, MyObj> cache = Collections.synchronizedMap(new LinkedHashMap<String, MyObj>(10, 1.5f, true));
    private long size = 0;
    private final long limit = Runtime.getRuntime().maxMemory() / 4;

    abstract int getSize(MyObj myObj);

    MyObj getFromMemoryCache(final String id) {

        if (!cache.containsKey(id)) {
            return null;
        }
        return cache.get(id);

    }

    void putToMemoryCache(final String id, final MyObj myObj) {
        if (cache.containsKey(id)) {
            size -= getSizeInBytes(cache.get(id));
        }
        cache.put(id, myObj);
        size += getSizeInBytes(myObj);
        checkSize();
    }

    private void checkSize() {
        if (size > limit) {
            final Iterator<Entry<String, MyObj>> iterator = cache.entrySet().iterator();
            while (iterator.hasNext()) {
                final Entry<String, MyObj> entry = iterator.next();
                size -= getSizeInBytes(entry.getValue());
                iterator.remove();
                if (size <= limit) {
                    break;
                }
            }
        }
    }

    void clear() {
        cache.clear();
        size = 0;
    }

    void clear(final String url) {
        if (cache.containsKey(url)) {
            cache.remove(url);
        }
    }

    private long getSizeInBytes(final MyObj myObj) {
        if (myObj == null) {
            return 0;
        }
        return getSize(myObj);
    }
}