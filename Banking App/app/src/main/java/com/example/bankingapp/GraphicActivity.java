package com.example.bankingapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.Nullable;

public class GraphicActivity extends AppCompatActivity {
        //Variable declaration
    public String balanceInt, savingsInt;
    public int spendInt;
    ArrayList<transactionStorage> transactionStorageArrayList = new ArrayList<>();
    private LineChart lineChart;
    private PieChart pieChart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceStat) {
        super.onCreate(savedInstanceStat);
        setContentView(R.layout.activity_visulisation);
        TextView spend = (TextView) findViewById(R.id.SpendTV);
        TextView savings = (TextView) findViewById(R.id.SavingsTV);
        TextView balance = (TextView) findViewById(R.id.balanceTV);
        //Get extras from intent for data.
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            savingsInt = extras.getString("savings");
            balanceInt = extras.getString("balance");
            transactionStorageArrayList = (ArrayList<transactionStorage>) extras.getSerializable("transactions");
        }

    savings.setText(savingsInt);
    balance.setText(balanceInt);


        //Data for charts to visualize data.
        pieChart = (PieChart) findViewById(R.id.pieChart);
        lineChart = (LineChart)findViewById(R.id.lineChart);

        ArrayList<PieEntry> pieValues = new ArrayList<>();
        ArrayList<Entry> lineValues = new ArrayList<>();

        pieChart.setUsePercentValues(true);

        if(transactionStorageArrayList != null){
            LocalDate currentDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate tempDate;
            Map<String, Double> pieMap = new HashMap<>();
            int counter = 3;
            int lineValue;
            double pieValue;
            String key;

            for(int n = 0; n<transactionStorageArrayList.size(); n++){
                if(transactionStorageArrayList.get(n).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(currentDate)){
                    spendInt += transactionStorageArrayList.get(n).calculateAmount();
                }
            }
            spend.setText(String.valueOf(spendInt));

            for(int i=0; i<4; i++) {
                lineValue = 0;
                for (int k = 0; k < transactionStorageArrayList.size(); k++){
                  tempDate = transactionStorageArrayList.get(k).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                  Log.d("","" + tempDate);
                    if(tempDate.getMonthValue() == currentDate.getMonthValue() - counter){
                        lineValue += transactionStorageArrayList.get(k).calculateAmount();
                    }
                    if(tempDate.getMonthValue() == currentDate.getMonthValue()){
                        if (pieMap.containsKey(transactionStorageArrayList.get(k).getCategory())) {
                            pieValue = pieMap.get(transactionStorageArrayList.get(k).getCategory());
                            pieMap.put(transactionStorageArrayList.get(k).getCategory(), pieValue + transactionStorageArrayList.get(k).calculateAmount());
                        }else{
                            pieMap.put(transactionStorageArrayList.get(k).getCategory(), transactionStorageArrayList.get(k).calculateAmount());
                        }
                    }
                }
                if(pieMap.keySet().toArray().length > i) {
                    key = pieMap.keySet().toArray()[i].toString();
                    pieValues.add(new PieEntry(Math.round(pieMap.get(key)), key));
                }
                  lineValues.add(new Entry(currentDate.getMonthValue()-counter, lineValue));
                  counter--;
                  }
            }
            PieDataSet dataSet = new PieDataSet(pieValues, "Categories");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

            PieData pieData = new PieData(dataSet);
            pieData.setValueTextSize(10f);
            pieData.setValueTextColor(Color.BLACK);
            pieChart.setData(pieData);

            LineDataSet set = new LineDataSet(lineValues, "Data Set");

            set.setFillAlpha(100);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);

            LineData data = new LineData(dataSets);

            lineChart.setData(data);
            lineChart.invalidate();

        Button bButton = (Button)findViewById(R.id.backButton);

        bButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(GraphicActivity.this, MenuActivity.class);
                backIntent.putExtra("transactions", transactionStorageArrayList);
                startActivity(backIntent);
            }
        });
        }


    }
