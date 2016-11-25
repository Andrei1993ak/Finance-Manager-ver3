package com.github.andrei1993ak.finances.model.backupUtil;

import android.content.Context;
import android.os.Environment;

import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.util.ContextHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class DBBackupUtils {

    private File sd;
    private File db;
    private File backUp;
    private final Context context = ContextHolder.getInstance().getContext();

    public boolean backupDB() {

        FileChannel src = null;
        FileChannel dst = null;
        try {
            sd = Environment.getExternalStorageDirectory();
            db = context.getDatabasePath(DBHelper.getInstance(context).getDatabaseName());
            if (sd.canWrite()) {
                backUp = new File(sd,DBHelper.getInstance(context).getDatabaseName()+".db");
                if (backUp.exists()){
                    backUp.createNewFile();
                }
                if (db.exists()) {
                    src = new FileInputStream(db).getChannel();
                    dst = new FileOutputStream(backUp).getChannel();
                    dst.transferFrom(src, 0, src.size());
                }
            }
        } catch (final IOException e) {
            return false;
        } finally {
            try {
                if (dst != null) {
                    dst.close();
                }
                if (src != null)
                    src.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean restoreDB(){
        FileChannel src = null;
        FileChannel dst = null;
        try {
            sd = Environment.getExternalStorageDirectory();
            db = context.getDatabasePath(DBHelper.getInstance(context).getDatabaseName());
            if (sd.canWrite()) {
                backUp = new File(sd,DBHelper.getInstance(context).getDatabaseName()+".db");
                if (!backUp.exists()){
                    return false;
                }
                if (db.exists()) {
                    src = new FileInputStream(backUp).getChannel();
                    dst = new FileOutputStream(db).getChannel();
                    dst.transferFrom(src, 0, src.size());
                }
            }
        } catch (final IOException e) {
            return false;
        } finally {
            try {
                if (dst != null) {
                    dst.close();
                }
                if (src != null)
                    src.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
