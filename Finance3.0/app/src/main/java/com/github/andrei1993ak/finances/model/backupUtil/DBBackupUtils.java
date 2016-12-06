package com.github.andrei1993ak.finances.model.backupUtil;

import android.content.Context;
import android.os.Environment;

import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.util.ContextHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Locale;

public class DBBackupUtils {

    private File storage;
    private File db;
    private File backUp;
    private final Context context = ContextHolder.getInstance().getContext();

    public boolean backupDB(final boolean isLocal) {

        FileChannel src = null;
        FileChannel dst = null;
        try {
            if (isLocal) {
                storage = ContextHolder.getInstance().getContext().getFilesDir();
            } else {
                storage = Environment.getExternalStorageDirectory();
            }
            db = context.getDatabasePath(DBHelper.getInstance(context).getDatabaseName());
            if (storage != null && storage.canWrite()) {
                backUp = new File(storage, DBHelper.getInstance(context).getDatabaseName() + ".db");
                if (!backUp.exists()) {
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

    public boolean restoreDB(final boolean isLocal) {
        FileChannel src = null;
        FileChannel dst = null;
        try {
            if (isLocal) {
                storage = ContextHolder.getInstance().getContext().getFilesDir();
            } else {
                storage = Environment.getExternalStorageDirectory();
            }
            db = context.getDatabasePath(DBHelper.getInstance(context).getDatabaseName());
            if (storage.canWrite()) {
                backUp = new File(storage, DBHelper.getInstance(context).getDatabaseName() + ".db");
                if (!backUp.exists()) {
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

    private HashSet<String> getExternalMounts() {
        final HashSet<String> out = new HashSet<>();
        final String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
        String s = "";
        InputStream is = null;
        try {
            final Process process = new ProcessBuilder().command("mount")
                    .redirectErrorStream(true).start();
            process.waitFor();
            is = process.getInputStream();
            final byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                s = s + new String(buffer);
            }
        } catch (final Exception e) {
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // parse output
        final String[] lines = s.split("\n");
        for (final String line : lines) {
            if (!line.toLowerCase(Locale.US).contains("asec")) {
                if (line.matches(reg)) {
                    final String[] parts = line.split(" ");
                    for (final String part : parts) {
                        if (part.startsWith("/"))
                            if (!part.toLowerCase(Locale.US).contains("vold"))
                                out.add(part);
                    }
                }
            }
        }
        return out;
    }
}
