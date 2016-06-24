package com.besaba.anvarov.ra_silenttimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmReceived extends BroadcastReceiver {
    public AlarmReceived() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        scheduleAlarms(context);
        throw new UnsupportedOperationException("Not yet implemented");
    }

    static void scheduleAlarms(Context context) {
        long NOW = Calendar.getInstance().getTimeInMillis();
        startAlarmService(context, NOW);
    }

    static void scheduleAlarms(Context context, long TIME) {
        startAlarmService(context, TIME);
    }

    static void startAlarmService(Context context, long UTIME) {
        Intent i = new Intent(context, AlarmService.class);
        i.setAction(AlarmService.SET_ALARM);
        i.putExtra("utime", UTIME);
        context.startService(i);
    }
}
