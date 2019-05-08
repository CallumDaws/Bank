package com.example.bankingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    //declare constants used with shared preferences
    public static final String ACCOUNT_BALANCE = "My_Balance";
    public static final String SAVINGS_BALANCE = "Savings_Balance";
    //declare variables for message, checking and savings balance
    String receivedString;
    public String chkBalance, savBalance;
    private ArrayList<transactionStorage> transactions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Bundle extras = getIntent().getExtras();
        //receive welcome msg from MainActivity
            if (extras != null) {
                receivedString = extras.getString("stringReference");
                transactions = (ArrayList<transactionStorage>)extras.getSerializable("transactions");
                Toast.makeText(MenuActivity.this, receivedString, Toast.LENGTH_LONG).show();
            }//end if

        //read checking and savings balances from shared preferences file
        getPrefs();



       //declare menu buttons
        Button checking_BT = (Button) findViewById(R.id.checkingButton);
        Button transfer_BT = (Button) findViewById(R.id.transferButton);
        Button student_button = (Button) findViewById(R.id.StudentButton);
        Button graphics_button = (Button) findViewById(R.id.VisulisationButton);
        final Button transaction_BT = (Button) findViewById(R.id.transactionButton);

       //register checking button with Event Listener class, and Event handler method
        checking_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //user wants to access checking account
                 Intent checkingIntent = new Intent(MenuActivity.this, TransactionActivity.class);
                 //send only data related to checking account
                 checkingIntent.putExtra("balance", chkBalance); //checking balance
                 checkingIntent.putExtra("savings", savBalance);
                 checkingIntent.putExtra("title", "Account"); //title for transaction activity
                 //display transaction activity screen
                 startActivity(checkingIntent);

            }
        });//end OnClickListener checking
        //register transaction button with Event Listener class, and Event handler method
        transaction_BT.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v) {
                Intent transactionIntent = new Intent(MenuActivity.this, transactionPage.class);
                transactionIntent.putExtra("balance",chkBalance);
                transactionIntent.putExtra("transactions",transactions);
                startActivity(transactionIntent);
            }
        });
        //register transfer button with Event Listener class, and Event handler method

        student_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent studentIntent = new Intent(MenuActivity.this, StudentFinancials.class);
                studentIntent.putExtra("transactions", transactions);
                startActivity(studentIntent);
            }
        });

        graphics_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent graphicIntent = new Intent(MenuActivity.this, GraphicActivity.class);
                graphicIntent.putExtra("transactions", transactions);
                graphicIntent.putExtra("balance", chkBalance); //checking balance
                graphicIntent.putExtra("savings", savBalance);
                startActivity(graphicIntent);
            }
        });


    }//end onCreate



    //function to retrieve current balances when program resumes
    protected void onResume() {
        super.onResume();
        getPrefs();

    }//end onResume
    //function to open shared preferences and retrieve current balances
    public void getPrefs() {
        //open shared preferences xml file
        SharedPreferences BalancePref = getSharedPreferences(MenuActivity.ACCOUNT_BALANCE, MODE_PRIVATE);
        Log.d("Balance", BalancePref.getAll().toString());
        //retrieve checking and savings balances if they are not null
        //or set balances to default value if they are null
        chkBalance = BalancePref.getString(ACCOUNT_BALANCE, "5000.00");
        savBalance = BalancePref.getString(SAVINGS_BALANCE, "0.00");

    }//end getPrefs
}//end MenuActivity
