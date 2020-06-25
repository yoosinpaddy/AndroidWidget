package com.trichain.androidwidget.room.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.trichain.androidwidget.room.tables.AlarmTable;

import java.util.List;

@Dao
public interface AlarmDao {
    @Query("SELECT * FROM AlarmTable")
    List<AlarmTable> getAllpeople();

    @Query("SELECT * FROM AlarmTable order by time DESC LIMIT 1")
    AlarmTable getLatestAlarm();

    @Query("SELECT COUNT(id) FROM AlarmTable WHERE 1")
    int getNumberofEventPeople();

    @Query("SELECT * FROM AlarmTable WHERE id=:id")
    AlarmTable getOnePerson(int id);

    @Query("SELECT * FROM AlarmTable WHERE 1")
    List<AlarmTable> getAllofEventPeople();

    @Insert
    void insert(AlarmTable peopleTable);

    @Delete
    void delete(AlarmTable peopleTable);

    @Update
    void update(AlarmTable peopleTable);
}
