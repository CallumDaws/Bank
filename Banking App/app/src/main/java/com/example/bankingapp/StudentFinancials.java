package com.example.bankingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.lang.reflect.Array;
import java.security.KeyStore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class StudentFinancials extends AppCompatActivity {

    private double incoming, spending, bills, savings, remaining;
    private String incomingS, spendingS, billsS, savingsS, remainingS, dateS;
    private LineChart lineGraph;
    private ArrayList<transactionStorage> transactionStorageArrayList = new ArrayList();
    private Date date;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(date == null){
            date = new Date();
        }
        //variable declaration
        setContentView(R.layout.activity_student_budgeting);
        Button currentB = (Button) findViewById(R.id.currentLoanBreakdown);
        final EditText incomingET = (EditText) findViewById(R.id.incomingText);
        final EditText spendingET = (EditText) findViewById(R.id.spendingText);
        final EditText billsET = (EditText) findViewById(R.id.billsText);
        final EditText savingsET = (EditText) findViewById(R.id.savingsText);
        final EditText remainingET = (EditText) findViewById(R.id.remainingText);
        final EditText dateET = (EditText) findViewById(R.id.dateText);
        incomingS = String.valueOf(incoming);
        spendingS = String.valueOf(spending);
        billsS = String.valueOf(bills);
        savingsS = String.valueOf(savings);
        remainingS = String.valueOf(remaining);
        incomingET.setText(incomingS);
        spendingET.setText(spendingS);
        billsET.setText(billsS);
        savingsET.setText(savingsS);
        remainingET.setText(remainingS);
        lineGraph = (LineChart) findViewById(R.id.linegraph);
        lineGraph.setScaleEnabled(false);


        Bundle extra = getIntent().getExtras();

        if(extra != null){
            transactionStorageArrayList = (ArrayList<transactionStorage>) extra.getSerializable("transactions");
        }
      //developed a button with a Listener to move to pages.
        incomingET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        keyCode == KeyEvent.KEYCODE_ENTER) {
                    incomingS = incomingET.getText().toString();
                    incoming = Double.valueOf(incomingS);
                    return true;
                }
                return false;
            }
        });
        //developed a button with a Listener to move to pages.
        spendingET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        keyCode == KeyEvent.KEYCODE_ENTER) {
                    spendingS = spendingET.getText().toString();
                    spending = Double.valueOf(spendingS);
                    return true;
                }
                return false;
            }
        });
        //developed a button with a Listener to move to pages.
        billsET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        keyCode == KeyEvent.KEYCODE_ENTER) {
                    billsS = billsET.getText().toString();
                    bills = Double.valueOf(billsS);
                    return true;
                }
                return false;
            }
        });
        //developed a button with a Listener to move to pages.
        savingsET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        keyCode == KeyEvent.KEYCODE_ENTER) {
                    savingsS = savingsET.getText().toString();
                    savings = Double.valueOf(savingsS);
                    return true;
                }
                return false;
            }
        });
        //developed a button with a Listener to move to pages.
        remainingET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        keyCode == KeyEvent.KEYCODE_ENTER) {
                    remainingS = remainingET.getText().toString();
                    remaining = Double.valueOf(remainingS);
                    return true;
                }
                return false;
            }
        });
        //developed a button with a Listener to move to pages.
        dateET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        keyCode == KeyEvent.KEYCODE_ENTER) {
                    dateS = dateET.getText().toString();
                    try {
                        date = new SimpleDateFormat("dd/MM/yyyy").parse(dateS);
                    } catch (ParseException e) {
                        Log.wtf("parse exception", dateS);
                    }
                    return true;
                }
                return false;
            }
        });
        //developed a button with a Listener to move to pages.
        currentB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent currentLoan = new Intent(StudentFinancials.this, CurrentLoanActivity.class);
                currentLoan.putExtra("remainingTime", date);
                currentLoan.putExtra("transactions", transactionStorageArrayList);
                startActivity(currentLoan);

            }
        });
        //Linegraph for loan spendature and the delcarations involved.
        ArrayList<Entry> values = new ArrayList<>();
        if (date != null) {
            LocalDate currentDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int month = localDate.getMonthValue();
            int currentMonth = currentDate.getMonthValue();
            int diff = currentMonth - month;
            int value = (int) remaining;
            int moving;
            if(diff!=0) {
                moving = value / diff;
            }else
                 moving = 200;

            for (int i = 0; i <= diff; i++) {
                values.add(new Entry(currentMonth + i, value));
                value = value - moving;
            }

            LineDataSet set = new LineDataSet(values, "Data Set");

            set.setFillAlpha(100);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);

            LineData data = new LineData(dataSets);

            lineGraph.setData(data);

        }
    }
}
