package com.besaba.anvarov.ra_silenttimer;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AlarmService extends IntentService {

    static String TAG_LOG = "SilentAlarm";

    private final int NOTIFY_GROUP = 1;

    public static final String SET_ALARM = "com.besaba.anvarov.ra_silenttimer.action.SET_ALARM";
    public static final String RUN_ALARM = "com.besaba.anvarov.ra_silenttimer.action.RUN_ALARM";

    public AlarmService() {
        super("AlarmService");
    }

   @Override
    protected void onHandleIntent(Intent intent) {
       Bundle extras = intent.getExtras();
       long TIME = extras.getLong("utime");
       // Установка сигнализации
       if (intent.getAction().equalsIgnoreCase(SET_ALARM)) {
           AlarmDB db = new AlarmDB(this);
           db.open();  // подключаюсь к БД
           Cursor c = db.select_NEXT_ALARM(TIME);  // получаю первое задание
           AlarmManager mgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
           Intent i = new Intent(this, AlarmService.class);
           if (c.getCount() > 0) {
               c.moveToFirst();
               int UTIMEi = c.getColumnIndex("utime");
               long UTIME = c.getLong(UTIMEi);
               i.putExtra("utime", UTIME);
               i.setAction(AlarmService.RUN_ALARM);
               PendingIntent pi = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
               mgr.cancel(pi);
               mgr.set(AlarmManager.RTC_WAKEUP, UTIME, pi);
           } else {
               PendingIntent pi = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
               mgr.cancel(pi);
           }
           c.close();
           db.close();
       } else
           // Сигнализация сработала!
           if (intent.getAction().equalsIgnoreCase(RUN_ALARM)) {
               AlarmDB db = new AlarmDB(this);
               db.open();
               Cursor c = db.select_ALARM_BY_UTIME(TIME);
               int MSGi = c.getColumnIndex("msg");
               String title, text;
               c.moveToFirst();
               if (!c.isAfterLast()) {
                   title = "Тревога!";
                   text = c.getString(MSGi);
                   String link = "Нажми меня... ";
                   //showNotification(title, text, link);
               }
               c.close();
               db.close();
               TIME += 500;
               AlarmReceived.scheduleAlarms(this, TIME);
           }
    }

}
