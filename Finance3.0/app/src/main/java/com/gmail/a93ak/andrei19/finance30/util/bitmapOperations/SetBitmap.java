package com.gmail.a93ak.andrei19.finance30.util.bitmapOperations;

import android.os.AsyncTask;

import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.OperationToFtp;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperQueueToFTP;
import com.gmail.a93ak.andrei19.finance30.util.ContextHolder;
import com.google.common.io.Files;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class SetBitmap extends AsyncTask<File, Void, Result<Integer>> {

    public static final String INTERNAL_PATH = "/data/data/com.gmail.a93ak.andrei19.finance30/files/images";

    public static final int FTP_ID = 9001;
    public static final int FILE_ID = 9002;
    public static final int ERROR_ID = 9000;

    private static final int BUFFER_SIZE = 4096;
    private static final String HOSTNAME = "93.125.42.84";
    private static final String USERNAME = "adk";
    private static final String PASSWORD = "1111111";

    private OnTaskCompleted listener;

    public SetBitmap(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected Result<Integer> doInBackground(File... params) {

        File file = params[0];
        int id = DBHelper.getInstance(ContextHolder.getInstance().getContext()).getNextId();

        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(HOSTNAME, 21);

            if (ftpClient.login(USERNAME, PASSWORD)) {

                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                FileInputStream in = new FileInputStream(file.getPath());
                String path = "images/" + String.valueOf(id) + ".jpg";
                ftpClient.storeFile(path, in);
                in.close();
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            String path = INTERNAL_PATH + "/" + String.valueOf(id) + ".jpg";
            File toFile = new File(path);
            try {
                Files.move(file,toFile);
                DBHelperQueueToFTP helper = DBHelperQueueToFTP.getInstance(DBHelper.getInstance(ContextHolder.getInstance().getContext()));
                OperationToFtp operationToFtp = new OperationToFtp();
                operationToFtp.setOperation(OperationToFtp.ADD);
                operationToFtp.setCost_id(id);
                helper.add(operationToFtp);
                return new Result<Integer>(FILE_ID, null);
            } catch (IOException e1) {
                return new Result<>(ERROR_ID,null);
        }

        }
        return new Result<Integer>(FTP_ID, null);
    }

    @Override
    protected void onPostExecute(Result result) {
        if (listener != null)
            listener.onTaskCompleted(result);
    }
}
