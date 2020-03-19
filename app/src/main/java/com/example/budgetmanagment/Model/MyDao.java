package com.example.budgetmanagment.Model;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyDao {

    @Insert
    public void addSpends(Spends spends);

    @Query("Select * from spends")
    public List<Spends> getSpends();

    @Update
    public void updateSpends(Spends user);

    @Delete
    public void deleteSpends(Spends spends);

   /* @Query("UPDATE spends SET label = :label, order_title= :title WHERE order_id =:id")
    void update(String label, String title, int id);*/

   @Query("UPDATE spends SET label = :label WHERE id =:id")
   void update(String label, int id);

    @Query("SELECT * FROM spends ORDER BY date DESC")
    List<Spends> getSortedlist();

    @Query("Select * from spends where month = :month")
    List<Spends> getSpendsMonthly(String month);

}
