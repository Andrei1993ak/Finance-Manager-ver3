package com.gmail.a93ak.andrei19.finance30.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class ImageUploader {

    private static final int BUFFER_SIZE = 4096;
    public static final String HOSTNAME = "93.125.42.84";
    public static final String USERNAME = "adk";
    public static final String PASSWORD = "1111111";

    public static void uploadToFTP(Bitmap bitmap, long id) {

        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File file = new File(extStorageDirectory, "temp.jpg");
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(HOSTNAME, 21);

            if (ftpClient.login(USERNAME, PASSWORD)) {

                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                FileInputStream in = new FileInputStream(file.getPath());
                String path = "images/"+String.valueOf(id)+".jpg";
                boolean result = ftpClient.storeFile(path, in);
                in.close();
                if (result)
                    Log.v("upload result", "succeeded");
                ftpClient.logout();
                ftpClient.disconnect();

            }
        } catch (Exception e) {
            Log.v("count", "error");
            e.printStackTrace();
        }

//
////        ftp://adk:1111111@93.125.42.84:21/images/image.jpg
////        String ftpUrl = "ftp://%s:%s@%s/images/%s.jpg;type=i";
//        String ftpUrl = "ftp://%s:%s@%s/images/;type=i";
//        String host = "93.125.42.84:21";
//        String user = "adk";
//        String pass = "1111111";
//        String uploadPath = String.valueOf(id);
//
//        ftpUrl = String.format(ftpUrl, user, pass, host);
//
//        try {
//            URL url = new URL(ftpUrl);
//            URLConnection conn = url.openConnection();
//            OutputStream outputStream = conn.getOutputStream();
//            int a = 12;
//            FileInputStream inputStream = new FileInputStream(file.getPath());
//
//            byte[] buffer = new byte[BUFFER_SIZE];
//            int bytesRead = -1;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//
//            inputStream.close();
//            outputStream.close();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

//    private File saveBitmap(Bitmap bmp) {
//        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
//        OutputStream outStream = null;
//        File file = new File(extStorageDirectory, "temp.png");
//        if (file.exists()) {
//            file.delete();
//            file = new File(extStorageDirectory, "temp.png");
//        }
//
//        try {
//            outStream = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
//            outStream.flush();
//            outStream.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return file;
//    }
}
