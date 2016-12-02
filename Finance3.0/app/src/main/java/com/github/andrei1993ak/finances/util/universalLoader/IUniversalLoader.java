package com.github.andrei1993ak.finances.util.universalLoader;

import java.io.File;

public interface IUniversalLoader<K, V> {

    int getSizeObj(K myObj);

    K decodeFromFile(File file, int PreSize);

    void set(K myObj, V destination);
}
