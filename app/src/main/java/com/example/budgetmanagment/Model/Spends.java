package com.example.budgetmanagment.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "spends")
public class Spends {

    @PrimaryKey
    private int id;
    @ColumnInfo(name = "label")
    private String label;
    @ColumnInfo(name = "price")
    private String price;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "month")
    private String month;

    @ColumnInfo(name = "year")
    private String year;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
