package com.example.budgetmanagment.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.budgetmanagment.Adpaters.SpendsAdpater;
import com.example.budgetmanagment.Model.MyAppDataBase;
import com.example.budgetmanagment.Model.Spends;
import com.example.budgetmanagment.R;

import java.util.ArrayList;
import java.util.List;

public class MoreSpends extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mMoreSpendRecycle;
    private Spinner mMonth;
    public static MyAppDataBase myAppDataBase;

    private List<Spends> spendsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_spends);

        mMoreSpendRecycle = findViewById(R.id.recycle_more_spends);
        mMonth = findViewById(R.id.month);
        myAppDataBase = Room.databaseBuilder(getApplicationContext(), MyAppDataBase.class, "userdb").allowMainThreadQueries().build();
        spendsList = new ArrayList<>();
        spendsList = BudgetPage.myAppDataBase.myDao().getSortedlist();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mMoreSpendRecycle.setLayoutManager(mLayoutManager);
        SpendsAdpater mAdapter = new SpendsAdpater(this, spendsList);
        mMoreSpendRecycle.setAdapter(mAdapter);
        onBindMonth();




    }

    private void onBindMonth() {
        String[] concepts = getResources().getStringArray(R.array.month);
        ArrayAdapter<String> adapterStates = new ArrayAdapter<String>(this,  R.layout.spinner_item, concepts);
        adapterStates.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mMonth.setAdapter(adapterStates);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
