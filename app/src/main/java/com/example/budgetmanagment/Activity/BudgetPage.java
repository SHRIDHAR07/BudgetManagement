package com.example.budgetmanagment.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.budgetmanagment.Adpaters.SpendsAdpater;
import com.example.budgetmanagment.Model.MyAppDataBase;
import com.example.budgetmanagment.Model.SmsListener;
import com.example.budgetmanagment.Model.SmsReceiver;
import com.example.budgetmanagment.Model.Spends;
import com.example.budgetmanagment.R;
import com.example.budgetmanagment.Utils.CustomDailog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BudgetPage extends AppCompatActivity {

    private SeekBar mSpendsInBar;
    private RecyclerView mRecycler;
    private Button mMoreSpends, mSetBudget;
    private TextView mTotalSpends;
    private FloatingActionButton mCreateSpends;
    private List<Spends> mThisMonthSpendsList = new ArrayList<>();
    public static List<Spends> mSpendsList = new ArrayList<>();
    private double TotalAmountSpendThisMonth = 0.00;
    private String mMatcher = "";
    public static MyAppDataBase myAppDataBase;
    List<Spends> spendsDatabaseList = new ArrayList<>();
    private int newAddedSpends = 0;
    private static final int MY_PERMISSIONS_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget__page);
        checkPermission();
        myAppDataBase = Room.databaseBuilder(getApplicationContext(), MyAppDataBase.class, "userdb").allowMainThreadQueries().build();
        intiView();
        clickListner();


    }

    private void clickListner() {
        mMoreSpends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BudgetPage.this, MoreSpends.class);
                startActivity(intent);
            }
        });

        mSetBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomDailog cdd = new CustomDailog(BudgetPage.this);
                cdd.show();
                cdd.setCancelable(false);

            }
        });
        mCreateSpends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BudgetPage.this, CreateSpends.class);
                startActivity(intent);
            }
        });
    }

    private void intiView() {
        mSpendsInBar = findViewById(R.id.spends);
        mRecycler = findViewById(R.id.recycle_view_spends);
        mMoreSpends = findViewById(R.id.more_spends);
        mSetBudget = findViewById(R.id.set_budget);
        mCreateSpends = findViewById(R.id.floatingActionButton2);
        mTotalSpends = findViewById(R.id.total_spends);


        Spends spends;
        mSpendsList = new ArrayList<>();
        for (Spends message : getAllSms()) {
            if (message.getLabel().contains("debited") || message.getLabel().contains("withdrawn") || message.getLabel().contains("Debit Card")) {
                Log.d("", "intiView: ");
                Pattern pattern;
                spends = new Spends();
                if (message.getLabel().contains("NETFLIX") || message.getLabel().contains("Debit Card")) {
                    pattern = Pattern.compile("Rs. [0-9]+");
                } else {
                    pattern = Pattern.compile("Rs. [0-9]+.[0-9][0-9]|^Rs.[0-9]+|INR [0-9]+.[0-9][0-9]");
                }

                Matcher matcher = pattern.matcher(message.getLabel());
                String amount = "";
                while (matcher.find()) {
                    amount = matcher.group();
                }
                if (!amount.isEmpty()) {
                    if (message.getLabel().contains("ATM")) {
                        spends.setLabel("ATM");
                    } else if (message.getLabel().contains("Petrol") || message.getLabel().contains("PETROL") || message.getLabel().contains("SERVICE")) {
                        spends.setLabel("Fuel");
                    } else if (message.getLabel().contains("movie") || message.getLabel().contains("NETFLIX") || message.getLabel().contains("AMAZON")) {
                        spends.setLabel("Entertainment");
                    } else {
                        spends.setLabel("Others");
                    }
                    if (amount.contains("Rs."))
                        amount = amount.replace("Rs.", "INR ");
                    else if (amount.contains("Rs. ")) {
                        amount = amount.replace("Rs. ", "INR ");
                    }

                    spends.setPrice(amount);
                    spends.setDate(message.getDate());
                    mSpendsList.add(spends);
                }

            }
        }

        spendsDatabaseList = myAppDataBase.myDao().getSpends();
        newAddedSpends = 0;
        Spends updatedSpends;
        newAddedSpends = mSpendsList.size() - spendsDatabaseList.size();

        for (Spends spends1 : mSpendsList) {
            newAddedSpends--;
            if (newAddedSpends == 0) {
                break;
            } else {
                spendsDatabaseList = myAppDataBase.myDao().getSpends();
                updatedSpends = spends1;
                Date date = new Date(Long.parseLong(updatedSpends.getDate()));
                SimpleDateFormat sdf = new SimpleDateFormat("MM");
                updatedSpends.setMonth(sdf.format(date));
                SimpleDateFormat year = new SimpleDateFormat("yyyy");
                updatedSpends.setYear(year.format(date));
                updatedSpends.setId(spendsDatabaseList.size() + 1);
                myAppDataBase.myDao().addSpends(updatedSpends);
            }
        }
        setSpendView();


        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.e("Message", messageText);
                Toast.makeText(BudgetPage.this, "Message: " + messageText, Toast.LENGTH_LONG).show();
                Pattern pattern = Pattern.compile("Rs. [0-9]+.[0-9][0-9]|Rs [0-9]+.[0-9][0-9]|INR [0-9]+.[0-9][0-9]");
                Matcher matcher = pattern.matcher(messageText);
                String otp = "";
                while (matcher.find()) {
                    otp = matcher.group();
                }
                Toast.makeText(BudgetPage.this, "Amount: " + otp, Toast.LENGTH_LONG).show();


            }
        });

    }

    public List<Spends> getAllSms() {
        List<Spends> lstSms = new ArrayList<>();
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official
                // CONTENT_URI
                // from docs
                new String[]{Telephony.Sms.Inbox.BODY, Telephony.Sms.Inbox.DATE}, // Select body text
                null, null, Telephony.Sms.Inbox.DEFAULT_SORT_ORDER); // Default
        // sort
        // order);
        int totalSMS = c.getCount();
        Spends spends;
        if (c.moveToFirst()) {

            for (int i = 0; i < totalSMS; i++) {

                spends = new Spends();
                spends.setLabel(c.getString(0));
                spends.setDate(c.getString(1));
                lstSms.add(spends);
                c.moveToNext();
            }
        } else {
            throw new RuntimeException("You have no SMS in Inbox");
        }
        c.close();

        return lstSms;
    }

    @Override
    protected void onStart() {

        setSpendView();
        super.onStart();
    }

    public void setSpendView() {

        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date Today = new Date();
        spendsDatabaseList = new ArrayList<>();
        mThisMonthSpendsList = new ArrayList<>();
        TotalAmountSpendThisMonth = 0;
        spendsDatabaseList = myAppDataBase.myDao().getSortedlist();

        for (Spends TotalSpend : spendsDatabaseList) {

            Date date = new Date(Long.parseLong(TotalSpend.getDate()));
            SimpleDateFormat sdf = new SimpleDateFormat("MM"); // the format of your date
            if (sdf.format(date).equalsIgnoreCase(dateFormat.format(Today))) {

                if (TotalSpend.getPrice().contains("INR")) {
                    mThisMonthSpendsList.add(TotalSpend);
                    TotalAmountSpendThisMonth += Double.parseDouble(TotalSpend.getPrice().replace("INR", ""));

                }

            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        mTotalSpends.setText(decimalFormat.format(TotalAmountSpendThisMonth));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(mLayoutManager);
        SpendsAdpater mAdapter = new SpendsAdpater(this, mThisMonthSpendsList);
        mRecycler.setAdapter(mAdapter);
    }

    protected void checkPermission() {
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED) {
            //ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)+ ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED) {
            // Do something, when permissions not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)){
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.READ_SMS)) {*/
                // If we should give explanation of requested permissions
                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("SMS READ");
                builder.setTitle("Please grant this permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                BudgetPage.this,
                                new String[]{

                                        /* Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                         Manifest.permission.READ_EXTERNAL_STORAGE,*/
                                        Manifest.permission.READ_SMS
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(
                        BudgetPage.this,
                        new String[]{

                                /*  Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                  Manifest.permission.READ_EXTERNAL_STORAGE,*/
                                Manifest.permission.READ_SMS
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
// When request is cancelled, the results array are empty
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Permissions granted.", Toast.LENGTH_SHORT).show();
                } else {
// Permissions are denied
                    Toast.makeText(this, "Permissions denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
