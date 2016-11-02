package com.gmail.a93ak.andrei19.finance30.control.Executors;

import android.database.Cursor;
import android.util.Log;

import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.PojoExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.OperationToFtp;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperCost;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperQueueToFTP;
import com.gmail.a93ak.andrei19.finance30.model.pojos.Cost;
import com.gmail.a93ak.andrei19.finance30.util.ContextHolder;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CostExecutor extends PojoExecutor<Cost> {

    public static final int KEY_RESULT_ADD = 701;
    public static final int KEY_RESULT_EDIT = 702;
    public static final int KEY_RESULT_DELETE = 703;
    public static final int KEY_RESULT_GET = 704;
    public static final int KEY_RESULT_GET_ALL = 705;
    public static final int KEY_RESULT_DELETE_ALL = 706;
    public static final int KEY_RESULT_GET_ALL_TO_LIST = 707;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID = 708;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_PURSE_ID = 709;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_DATES = 710;

    private static final String HOSTNAME = "93.125.42.84";
    private static final String USERNAME = "adk";
    private static final String PASSWORD = "1111111";


    public static final String INTERNAL_PATH = "/data/data/com.gmail.a93ak.andrei19.finance30/files/images";

    public CostExecutor(OnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<Cost> getPojo(long id) {
        return new Result<>(KEY_RESULT_GET, DBHelperCost.getInstance(DBHelper.getInstance(context)).get(id));
    }

    @Override
    public Result<Long> addPojo(Cost cost) {
        return new Result<>(KEY_RESULT_ADD, DBHelperCost.getInstance(DBHelper.getInstance(context)).add(cost));
    }

    @Override
    public Result<Integer> deletePojo(long id) {

        DBHelperCost costDbHelper = DBHelperCost.getInstance(DBHelper.getInstance(ContextHolder.getInstance().getContext()));
        Cost cost = costDbHelper.get(id);
        DBHelperQueueToFTP queueToFTPHelper = DBHelperQueueToFTP.getInstance(DBHelper.getInstance(ContextHolder.getInstance().getContext()));

        if (cost.getPhoto() == Cost.PHOTO_IN_STORAGE) {
            File file = new File(INTERNAL_PATH + "/" + String.valueOf(id) + ".jpg");
            boolean result = file.delete();
            queueToFTPHelper.delete(id);
            return new Result<>(KEY_RESULT_DELETE, costDbHelper.delete(id));
        } else if (cost.getPhoto() == Cost.PHOTO_IN_FTP) {
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
                    queueToFTPHelper.delete(id);
                    return new Result<>(KEY_RESULT_DELETE, costDbHelper.delete(id));
                }
            } catch (Exception e) {
                queueToFTPHelper.add(new OperationToFtp(id, OperationToFtp.DELETE));
                return new Result<>(KEY_RESULT_DELETE, costDbHelper.delete(id));
            }
        }

        return new Result<>(KEY_RESULT_DELETE, DBHelperCost.getInstance(DBHelper.getInstance(context)).delete(id));
    }

    @Override
    public Result<Integer> updatePojo(Cost cost) {
        return new Result<>(KEY_RESULT_EDIT, DBHelperCost.getInstance(DBHelper.getInstance(context)).update(cost));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, DBHelperCost.getInstance(DBHelper.getInstance(context)).getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return new Result<>(KEY_RESULT_DELETE_ALL, DBHelperCost.getInstance(DBHelper.getInstance(context)).deleteAll());
    }

    @Override
    public Result<List<Cost>> getAllToList(int selection) {
        return new Result<>(KEY_RESULT_DELETE_ALL, DBHelperCost.getInstance(DBHelper.getInstance(context)).getAllToList());
    }

    @Override
    protected Result<List<Cost>> getAllToListByDates(ArrayList<Long> diapason) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_DATES, DBHelperCost.getInstance(DBHelper.getInstance(context)).getAllToListByDates(diapason.get(0), diapason.get(1)));
    }

    @Override
    protected Result<List<Cost>> getAllToListByPurseId(Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_PURSE_ID, DBHelperCost.getInstance(DBHelper.getInstance(context)).getAllToListByPurseId(id));
    }

    @Override
    protected Result<List<Cost>> getAllToListByCategoryId(Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID, DBHelperCost.getInstance(DBHelper.getInstance(context)).getAllToListByCategoryId(id));
    }
}
