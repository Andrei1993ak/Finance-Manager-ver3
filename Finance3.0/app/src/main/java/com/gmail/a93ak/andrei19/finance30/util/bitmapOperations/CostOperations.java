package com.gmail.a93ak.andrei19.finance30.util.bitmapOperations;

import android.util.Pair;

import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;

import java.io.File;

public class CostOperations {

    public static void uploadBitmap(File file, OnTaskCompleted onTaskCompleted){
        new SetBitmap(onTaskCompleted).execute(file);
    }

    public static void changeBitmap(long id, File file, OnTaskCompleted ontaskCompleted) {
        new UpdateBitmap(ontaskCompleted).execute(new Pair<File, Long>(file,id));
    }
}
