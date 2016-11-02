package com.gmail.a93ak.andrei19.finance30.util.bitmapOperations;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;


public class DeleteBitmap extends AsyncTask<Long, Void, Void> {

    private static final String HOSTNAME = "93.125.42.84";
    private static final String USERNAME = "adk";
    private static final String PASSWORD = "1111111";

    @Override
    protected Void doInBackground(Long... params) {

        long id = params[0];

        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(HOSTNAME, 21);

            if (ftpClient.login(USERNAME, PASSWORD)) {

                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                String path = "images/" + String.valueOf(id) + ".jpg";
                boolean result = ftpClient.deleteFile(path);
                if (result)
                    Log.v("upload result", "succeeded");
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
