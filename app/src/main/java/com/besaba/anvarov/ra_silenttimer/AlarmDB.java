package com.besaba.anvarov.ra_silenttimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by arr on 14.02.16.
 */
public class AlarmDB {
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase database;
    private DatabaseOpenHelper databaseOpenHelper;

    private static final String TB_SHEDULER = "tbSheduler";
    private static final String SQL_CREATE_TB_SHEDULER = "CREATE TABLE " + TB_SHEDULER +
            " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, utime NUMERIC, msg TEXT );";

    private static final String SQL_SELECT_NEXT_ALARM = "SELECT utime FROM " + TB_SHEDULER + " WHERE utime >= %d ORDER BY utime LIMIT 1";
    private static final String SQL_ALARM_BY_UTIME    = "SELECT msg FROM " + TB_SHEDULER + " WHERE utime = %d";

    public void insertAlarm(long UTIME, String MSG) {
        ContentValues data = new ContentValues();
        data.put("utime", UTIME);
        data.put("msg", MSG);

        open();
        database.insert(TB_SHEDULER, null, data);
        close();
    }

    public Cursor select_NEXT_ALARM(long UTIME) {
        return database.rawQuery(String.format(SQL_SELECT_NEXT_ALARM, UTIME), null);
    }

    public Cursor select_ALARM_BY_UTIME(long UTIME) {
        return database.rawQuery(String.format(SQL_ALARM_BY_UTIME, UTIME), null);
    }

    public AlarmDB(Context context) {
        String DATABASE_NAME = "alarm";
        databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException {
        database = databaseOpenHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) database.close();
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper {

        public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TB_SHEDULER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
