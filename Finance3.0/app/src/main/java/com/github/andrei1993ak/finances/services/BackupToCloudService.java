package com.github.andrei1993ak.finances.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.github.andrei1993ak.finances.model.backupUtil.DBBackupUtils;

public class BackupToCloudService extends Service {

    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        new DBBackupUtils().backupToCloud(null);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }
}
