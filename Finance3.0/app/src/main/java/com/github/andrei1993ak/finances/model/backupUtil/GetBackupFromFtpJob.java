package com.github.andrei1993ak.finances.model.backupUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.github.andrei1993ak.finances.model.DBHelper;
import com.github.andrei1993ak.finances.util.Constants;
import com.github.andrei1993ak.finances.util.ContextHolder;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class GetBackupFromFtpJob extends AsyncTask<Void, Void, Boolean> {

    private static final String HOSTNAME = "213.184.250.210";
    private static final String USERNAME = "and";
    private static final String PASSWORD = "and";

    private final OnBackupOperationCompleted listener;

    public GetBackupFromFtpJob(final OnBackupOperationCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(final Void... voids) {
        FTPClient ftpClient = null;
        final Context context = ContextHolder.getInstance().getContext();
        FileOutputStream output = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(HOSTNAME, 21);
            if (ftpClient.login(USERNAME, PASSWORD)) {
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                final File db = context.getDatabasePath(DBHelper.getInstance(context).getDatabaseName());
                final SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
                final String email = sharedPreferences.getString(Constants.GOOGLE_ACC_EMAIL, null);
                if (email == null) {
                    return false;
                }
                final String path = "Backups/" + email + ".db";
                output = new FileOutputStream(db.getPath());
                ftpClient.retrieveFile(path, output);
            }
        } catch (final Exception e) {
            return false;
        } finally {
            try {
                if (ftpClient != null) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            if (output != null) {
                try {
                    output.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean result) {
        if (listener != null)
            listener.onBackupCompleted(result);
    }
}