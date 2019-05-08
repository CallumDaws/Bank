package com.example.bankingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class TransactionActivity extends AppCompatActivity {
    //declare constant of shared preferences file
    public static final String MY_BALANCE = "My_Balance";
    public static final String SAVINGS_BALANCE = "Savings_Balance";

    //declare variables
    public String receivedBalance, receivedSavings, receivedTitle; //data received from menu activity
    public double BalanceD, SBalance;
    public double DepositEntered, NewBalance, WithdrawEntered;
    TextView BalanceTV, TitleTV, SavingsTv;
    public DecimalFormat currency = new DecimalFormat("$###,##0.00"); //decimal formatting
    SharedPreferences.Editor myEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);


        //receive balance, key and title from menu activity
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            receivedBalance = extras.getString("balance");
            receivedSavings = extras.getString("savings");
            receivedTitle = extras.getString("title");

        }//end if
        //set layout title depending on data that was received to checking or savings account
        TitleTV = (TextView) findViewById(R.id.titleTextView);
        TitleTV.setText(receivedTitle);

        //set current balance of account or savings account
        BalanceTV = (TextView) findViewById(R.id.BalanceTextView);
        SavingsTv = (TextView) findViewById(R.id.savingsTextView);
        BalanceD = Double.parseDouble(String.valueOf(receivedBalance));
        SBalance = Double.parseDouble(String.valueOf(receivedSavings));
        Log.d("savings",String.valueOf(SBalance));
        BalanceTV.setText(String.valueOf(currency.format(BalanceD)));
        SavingsTv.setText(String.valueOf(currency.format(SBalance)));

        //declare deposit button
        Button DepositB = (Button) findViewById(R.id.DepositButton);
        Button WithdrawB = (Button) findViewById(R.id.WithdrawButton);
        Button BackB = (Button)findViewById(R.id.backButton);
        //declare user deposit input amount
        final EditText DepositET = (EditText) findViewById(R.id.DepositEditText);

        BackB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent backIntent = new Intent(TransactionActivity.this, MenuActivity.class);
                startActivity(backIntent);
            }
        });
        //register deposit button with Event Listener class, and Event handler method
        WithdrawB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if deposit field is not empty, get deposit amount
                if (!TextUtils.isEmpty(DepositET.getText())) {
                    DepositEntered = Double.parseDouble(String.valueOf(DepositET.getText()));
                    if (SBalance >= DepositEntered) {
                        //create deposit object
                        Deposit d = new Deposit();
                        d.setBalance(BalanceD);
                        d.setDeposit(DepositEntered);

                        //calculate new balance
                        NewBalance = d.getNewBalance();
                        SBalance = SBalance - DepositEntered;

                        BalanceTV.setText(String.valueOf(currency.format(NewBalance)));
                        SavingsTv.setText(String.valueOf(currency.format((SBalance))));
                        BalanceD = NewBalance;
                        //reset user deposit amount to zero
                        DepositEntered = 0;
                    } else {//end if
                        Toast.makeText(TransactionActivity.this, "You do not have enough money in Savings to Withdraw this amount", Toast.LENGTH_LONG).show();
                    }
                }
                //deposit filed is empty, prompt user to enter deposit amount
                else {

                    Toast.makeText(TransactionActivity.this, "Please enter deposit amount and try again!", Toast.LENGTH_LONG).show();
                }//end else
                //clear deposit field
                DepositET.setText(null);
            }
        });//end DepositB OnClick

        //declare withdraw button
        //declare user withdraw input amount
        final EditText WithdrawET = (EditText) findViewById(R.id.WithdrawEditText);

        //register withdraw button with Event Listener class, and Event handler method
        DepositB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if withdraw field is not empty, get withdraw amount
                if (!TextUtils.isEmpty(WithdrawET.getText())) {

                    WithdrawEntered = Double.parseDouble(String.valueOf(WithdrawET.getText()));

                    //check if there's enough money to withdraw in the acoount
                    if (BalanceD >= WithdrawEntered) {
                        //create withdraw object
                        Withdraw wd = new Withdraw();
                        wd.setBalance(BalanceD);
                        wd.setWithdraw(WithdrawEntered);

                        //calculate new balance
                        NewBalance = wd.getNewBalance();
                        SBalance = SBalance + WithdrawEntered;

                        BalanceTV.setText(String.valueOf(currency.format(NewBalance)));
                        SavingsTv.setText(String.valueOf(currency.format(SBalance)));
                        BalanceD = NewBalance;
                        //reset user withdraw amount to zero
                        WithdrawEntered = 0;
                    }//end 2nd if
                    //there's not enough money in the account, prompt user for valid input
                    else
                        Toast.makeText(TransactionActivity.this, "Insufficient funds! Please enter a valid withdraw amount and try again!", Toast.LENGTH_LONG).show();
                }//end 1st if
                //withdraw filed is empty, prompt user to enter withdraw amount
                else {
                    Toast.makeText(TransactionActivity.this, "Nothing entered! Please enter withdraw amount and try again!", Toast.LENGTH_LONG).show();
                }

                //clear withdraw field
                WithdrawET.setText(null);
            }
        });//end Withdraw onClick




    }//end onCreate
    //function to open and edit shared preferences file
    protected void onPause() {

        super.onPause();
        //open shared preferences xml file
        myEditor = getSharedPreferences(MY_BALANCE, MODE_PRIVATE).edit();


        //save new checking or savings balance
        myEditor.putString(MY_BALANCE, String.valueOf(BalanceD));
        myEditor.putString(SAVINGS_BALANCE, String.valueOf(SBalance));
        myEditor.apply();

    }//end onPause

}//end  TransactionActivity



