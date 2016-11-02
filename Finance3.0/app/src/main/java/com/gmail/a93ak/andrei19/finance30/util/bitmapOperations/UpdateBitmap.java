package com.gmail.a93ak.andrei19.finance30.util.bitmapOperations;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.util.ContextHolder;
import com.gmail.a93ak.andrei19.finance30.util.UniversalLoader.Loaders.BitmapLoader;
import com.gmail.a93ak.andrei19.finance30.util.UniversalLoader.UniversalLoader;
import com.gmail.a93ak.andrei19.finance30.view.Activities.CostActivity;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;


public class UpdateBitmap extends AsyncTask<Pair<File, Long>, Void, Result<Integer>> {

    public static final int FTP_ID = 8001;
    public static final int FILE_ID = 8002;
    public static final int ERROR = 8000;

    private static final int BUFFER_SIZE = 4096;
    private static final String HOSTNAME = "93.125.42.84";
    private static final String USERNAME = "adk";
    private static final String PASSWORD = "1111111";

    private OnTaskCompleted listener;

    public UpdateBitmap(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected Result<Integer> doInBackground(Pair<File, Long>... params) {

        File file = params[0].first;
        long id = params[0].second;

        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(HOSTNAME, 21);

            if (ftpClient.login(USERNAME, PASSWORD)) {

                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                FileInputStream in = new FileInputStream(file.getPath());
                String path = "images/" + String.valueOf(id) + ".jpg";
                ftpClient.deleteFile(path);
                BitmapLoader.getInstance(ContextHolder.getInstance().getContext()).clearCashes(CostActivity.FTP_PATH + String.valueOf(id) + ".jpg");
                boolean result = ftpClient.storeFile(path, in);
                in.close();
                if (result)
                    Log.v("upload result", "succeeded");
                ftpClient.logout();
                ftpClient.disconnect();

            }
        } catch (Exception e) {
            return new Result<Integer>(ERROR, null);
        }
        return new Result<Integer>(FTP_ID, null);
    }

    @Override
    protected void onPostExecute(Result result) {
        if (listener != null)
            listener.onTaskCompleted(result);
    }
}
