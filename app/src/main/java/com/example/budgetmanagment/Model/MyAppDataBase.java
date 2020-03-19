package com.example.budgetmanagment.Model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Spends.class},version = 1)
public abstract class MyAppDataBase extends RoomDatabase {
    public abstract MyDao myDao();

}
