package com.trichain.androidwidget.room.config;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.trichain.androidwidget.room.daos.AlarmDao;
import com.trichain.androidwidget.room.tables.AlarmTable;


@Database(entities = {AlarmTable.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AlarmDao alarmDao();


}
