package org.thoughtcrime.securesms.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.thoughtcrime.securesms.dependencies.ApplicationDependencies;
import org.thoughtcrime.securesms.util.ServiceUtil;

public class ExpirationListener extends ExportedBroadcastReceiver {

  @Override
  public void onReceiveUnlock(Context context, Intent intent) {
    ApplicationDependencies.getExpiringMessageManager().checkSchedule();
  }

  public static void setAlarm(Context context, long waitTimeMillis) {
    PendingIntent pendingIntent = buildExpirationPendingIntent(context);
    AlarmManager  alarmManager  = ServiceUtil.getAlarmManager(context);

    alarmManager.cancel(pendingIntent);
    alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + waitTimeMillis, pendingIntent);
  }

  public static void cancelAlarm(Context context) {
    ServiceUtil.getAlarmManager(context).cancel(buildExpirationPendingIntent(context));
  }

  private static PendingIntent buildExpirationPendingIntent(Context context) {
    Intent intent = new Intent(context, ExpirationListener.class);
    return PendingIntent.getBroadcast(context, 0, intent, 0);
  }
}
