package com.example.budgetmanagment.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetmanagment.R;

import java.util.Calendar;


public class CustomDailog extends Dialog implements View.OnClickListener {

    public Activity This;
    public Dialog d;
    public Button Set, Cancel;
    private SeekBar mSeekbar;
    private EditText mEditText;
    private TextView mSafeToSpend;
    private int mSpendsPerDay = 0;
    public CustomDailog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.This = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dailog);

        Set = findViewById(R.id.set);
        Cancel = findViewById(R.id.cancel);
        mSeekbar = findViewById(R.id.dailog_budget_seekbar);
        mEditText = findViewById(R.id.budget_value);
        mSafeToSpend = This.findViewById(R.id.safe_to_spend);
        Set.setOnClickListener(this);
        Cancel.setOnClickListener(this);

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mEditText.setText(""+progress);
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!mEditText.getText().toString().isEmpty()){

                    if(mEditText.getText().toString().length()>7){
                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(This);
                        builder.setMessage("Budget can not exceed above 1Cr ");
                        builder.setTitle("Alert");
                        builder.setPositiveButton(
                                "Ok",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {

                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        // Show the Alert Dialog box
                        alertDialog.show();
                        dismiss();
                    }else{
                        mSeekbar.setProgress(Integer.parseInt(mEditText.getText().toString()));
                    }

                }



            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set:
                mSpendsPerDay = mSeekbar.getProgress();
                spendsPerday();
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    public  void spendsPerday() {
        Calendar calendar = Calendar.getInstance();
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int daysLeft = lastDay - currentDay;
        if (mSpendsPerDay != 0) {
            mSafeToSpend.setText("Safe to spends ₹ " + mSpendsPerDay / daysLeft + "/day");
        }
    }
}