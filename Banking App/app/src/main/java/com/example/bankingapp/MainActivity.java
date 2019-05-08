package com.example.bankingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    //variable declaration
    String UserName, Password;
    int loginCounter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //display welcome msg when app starts
        Toast.makeText(MainActivity.this, "Welcome User. Please enter your Username and Password.", Toast.LENGTH_LONG).show();

        //reference Button, User Name and Password
        Button myButton = (Button) findViewById(R.id.loginButton);
        final EditText myUserName = (EditText) findViewById(R.id.usernameEditText);
        final EditText myPassword = (EditText) findViewById(R.id.passwordEditText);

        //register button with Event Listener class, and Event handler method
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //get user input
                UserName = myUserName.getText().toString();
                Password = myPassword.getText().toString();
                readTransactions();


                //check how many times unregistered user tried to log in with wrong credentials

                //check if username and password are correct
                if (UserName.equals("Admin") && Password.equals("Password")) {
                    //user is registered, display menu layout and registered user msg

                    Intent myIntent = new Intent(MainActivity.this, MenuActivity.class);
                    myIntent.putExtra("stringReference", "Access Granted!");
                    myIntent.putExtra("transactions", transactions);
                    //display menu activity screen
                    startActivity(myIntent);

                }//end if
                //check login attempts counter
                else if (loginCounter != 1) {
                    //unregistered user, display unregistered user msg and decrease login counter
                    loginCounter--;

                    Toast.makeText(MainActivity.this, "Access Denied! Please try again.You have " + loginCounter + " attempt(s) remaining", Toast.LENGTH_LONG).show();
                }//end else if
                else {
                    //3 login attempts are up, close app
                    Toast.makeText(MainActivity.this, "Access Denied!", Toast.LENGTH_LONG).show();
                    finish();
                }//end else

            }//end onClick
        });//end setOnClickListener
    }//end onCreate

    private ArrayList<transactionStorage> transactions = new ArrayList<>();
    private void readTransactions(){
        InputStream is = getResources().openRawResource(R.raw.banksample);
        Reader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        try {
            //Avoid Headers
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                //Split by ','

                transactionStorage transactionObject = new transactionStorage();
                if(nextRecord[0].length() > 0){
                    transactionObject.setMoneyIn(Double.parseDouble(nextRecord[0]));
                }else{
                    transactionObject.setMoneyIn(0);
                }
                if(nextRecord[1].length() > 0){
                    transactionObject.setMoneyOut(Double.parseDouble(nextRecord[1]));
                }else{
                    transactionObject.setMoneyOut(0);
                }
                String sDate = nextRecord[2];
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(sDate);
                transactionObject.setDate(date);
                transactionObject.setDescription(nextRecord[3]);
                if(transactionObject.getCategory() == null) {
                    transactionObject.setCategory("Category");
                }else{
                    transactionObject.setCategory(transactionObject.getCategory());
                }
                if(nextRecord[4].length() > 0){
                    transactionObject.setReference(Integer.parseInt(nextRecord[4]));
                }else {
                    transactionObject.setReference(0);
                }

                transactions.add(transactionObject);

                Log.d("transactionPage", "Created" + transactionObject);
            }
        } catch (Exception e){
            Log.wtf("transactionPage", "File reading error", e );
        }
    }

}//end MainActivity
