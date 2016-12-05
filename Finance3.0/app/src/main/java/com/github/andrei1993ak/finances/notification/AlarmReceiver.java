package com.github.andrei1993ak.finances.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.github.andrei1993ak.finances.R;
import com.github.andrei1993ak.finances.app.ZeroActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, ZeroActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(contentIntent);
        builder.setContentTitle(context.getResources().getString(R.string.app_name));
        builder.setContentText(context.getResources().getString(R.string.notification_text));
        builder.setSmallIcon(R.drawable.fin);
        builder.setAutoCancel(true);
        notificationManager.notify(R.string.alarm_label, builder.build());

    }
}
