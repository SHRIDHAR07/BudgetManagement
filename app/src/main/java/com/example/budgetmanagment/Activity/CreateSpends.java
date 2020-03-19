package com.example.budgetmanagment.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.budgetmanagment.Model.Spends;
import com.example.budgetmanagment.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CreateSpends extends AppCompatActivity {


    private Button mSave,mSaveAnother;
    private EditText mSpendsFor,mAmount,mDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spends);

        mSave = findViewById(R.id.save);
        mSpendsFor = findViewById(R.id.spends_for);
        mAmount = findViewById(R.id.amount);
        mDate = findViewById(R.id.date);
        mSaveAnother = findViewById(R.id.save_another);
        mAmount.setEnabled(true);
        mDate.setEnabled(true);

        final Bundle bundle = getIntent().getExtras();
        if(bundle !=null){

            mSpendsFor.setText(bundle.getString("label"));
            mAmount.setText(bundle.getString("amount"));
            Date date = new Date(Long.parseLong(bundle.getString("date"))); // *1000 is to convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy "); // the format of your date
            mDate.setText(""+sdf.format(date));
            mAmount.setEnabled(false);
            mDate.setEnabled(false);
        }


        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spends spends = new Spends();
                if(!mSpendsFor.getText().toString().isEmpty()&& !mDate.getText().toString().isEmpty()){
                    List<Spends> userList = BudgetPage.myAppDataBase.myDao().getSpends();

                    if(userList.isEmpty())
                        spends.setId(1);
                    else{
                        spends.setId(userList.size()+1);
                    }
                    spends.setLabel(mSpendsFor.getText().toString());
                    spends.setDate(mDate.getText().toString());
                    spends.setPrice(mAmount.getText().toString());

                    if(bundle != null){
                        if(bundle.getString("id")!= null){
                            BudgetPage.myAppDataBase.myDao().update(mSpendsFor.getText().toString(),Integer.parseInt(bundle.getString("id")));
                        }
                    }else{
                        BudgetPage.myAppDataBase.myDao().addSpends(spends);
                    }


                    Toast.makeText(CreateSpends.this,"Data inserted ",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(CreateSpends.this,"Enter the valid fields",Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });

        mSaveAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Spends> userList = BudgetPage.myAppDataBase.myDao().getSpends();


            }
        });


    }
}
