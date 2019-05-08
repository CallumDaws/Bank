package com.example.bankingapp;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrentLoanActivity extends AppCompatActivity {
    //Variable declaration
    EditText initialTxt, remainingTxt, dateTxt;
    TextView category1, category2, category3, category4, category5;
    TextView value1, value2, value3, value4, value5;
    public double initial, remaining;
    public String recievedInitial, recievedRemaining;
    public Date timeUntil;
    public DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    ArrayList<transactionStorage> transactions = new ArrayList<>();
    Map<String, Double> catagoryMap = new HashMap<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_loan_breakdown);
//Intent extras from other pages.
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            recievedInitial = extras.getString("initial");
            recievedRemaining = extras.getString("remaining");
            transactions = (ArrayList<transactionStorage>) extras.getSerializable("transactions");
            try {
                timeUntil = dateFormat.parse(extras.get("remainingTime").toString());
            } catch (ParseException e) {
                Log.d("na", "onCreate: not working");
            }
        }
        //Recyclerview declaration
        recyclerView = (RecyclerView) findViewById(R.id.TransRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        mAdapter = new transAdapter(this, transactions);
        recyclerView.setAdapter(mAdapter);

    //Declaration of textviews
        Date current = new Date();
        dateTxt = (EditText) findViewById(R.id.timeUntilPaymentTxt);
        initialTxt = (EditText) findViewById(R.id.initialText);
        remainingTxt = (EditText) findViewById(R.id.remainingText);
        category1 = (TextView) findViewById(R.id.catagoryText1);
        category2 = (TextView) findViewById(R.id.catagoryText2);
        category3 = (TextView) findViewById(R.id.catagoryText3);
        category4 = (TextView) findViewById(R.id.catagoryText4);
        category5 = (TextView) findViewById(R.id.catagoryText5);
        value1 = (TextView) findViewById(R.id.valueText1);
        value2 = (TextView) findViewById(R.id.valueText2);
        value3 = (TextView) findViewById(R.id.valueText3);
        value4 = (TextView) findViewById(R.id.valueText4);
        value5 = (TextView) findViewById(R.id.valueText5);


        if (timeUntil != null) {
            Long diff = timeUntil.getTime() - current.getTime();
            dateTxt.setText(diff.toString());
        }
       //Putting values into a map to allocated values to the categories
        if (transactions != null) {
            for (int i = 0; i < transactions.size(); i++) {
                if (catagoryMap.containsKey(transactions.get(i).getCategory())) {
                    double tempvalue = catagoryMap.get(transactions.get(i).getCategory());
                    catagoryMap.put(transactions.get(i).getCategory(), tempvalue + transactions.get(i).calculateAmount());
                } else {
                    catagoryMap.put(transactions.get(i).getCategory(), transactions.get(i).calculateAmount());
                }
            }
        }

        //Input stream to turn the category map into a sorted list
        List<String> sortedList = catagoryMap
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        //Switch and if statement to put the data from the sorted list into the application itself.
        if (sortedList != null) {
            switch (sortedList.size()) {
                case 1:
                    category1.setText(sortedList.get(0));
                    value1.setText(String.valueOf(catagoryMap.get(sortedList.get(0))));
                    break;
                case 2:
                    category1.setText(sortedList.get(0));
                    value1.setText(String.valueOf(catagoryMap.get(sortedList.get(0))));
                    category2.setText(sortedList.get(1));
                    value2.setText(String.valueOf(catagoryMap.get(sortedList.get(1))));
                    break;

                case 3:
                    category1.setText(sortedList.get(0));
                    value1.setText(String.valueOf(catagoryMap.get(sortedList.get(0))));
                    category2.setText(sortedList.get(1));
                    value2.setText(String.valueOf(catagoryMap.get(sortedList.get(1))));
                    category3.setText(sortedList.get(2));
                    value3.setText(String.valueOf(catagoryMap.get(sortedList.get(2))));
                    break;

                case 4:
                    category1.setText(sortedList.get(0));
                    value1.setText(String.valueOf(catagoryMap.get(sortedList.get(0))));
                    category2.setText(sortedList.get(1));
                    value2.setText(String.valueOf(catagoryMap.get(sortedList.get(1))));
                    category3.setText(sortedList.get(2));
                    value3.setText(String.valueOf(catagoryMap.get(sortedList.get(2))));
                    category4.setText(sortedList.get(3));
                    value4.setText(String.valueOf(catagoryMap.get(sortedList.get(3))));
                    break;
            }
            if (sortedList.size() >= 5) {
                category1.setText(sortedList.get(0));
                value1.setText(String.valueOf(catagoryMap.get(sortedList.get(0))));
                category2.setText(sortedList.get(1));
                value2.setText(String.valueOf(catagoryMap.get(sortedList.get(1))));
                category3.setText(sortedList.get(2));
                value3.setText(String.valueOf(catagoryMap.get(sortedList.get(2))));
                category4.setText(sortedList.get(3));
                value4.setText(String.valueOf(catagoryMap.get(sortedList.get(3))));
                category5.setText(sortedList.get(4));
                value5.setText(String.valueOf(catagoryMap.get(sortedList.get(4))));
            }
        } else {
            category1.setText("No Data Found");
        }

        if (initial > 0) {
            initialTxt.setText(String.valueOf(initial));
        }
        if (remaining > 0) {
            remainingTxt.setText(String.valueOf(remaining));
        }
    }
    //Adapter for displaying the recyclerview for transactions. same functionality at transaction class.

    class transAdapter extends RecyclerView.Adapter<CurrentLoanActivity.transactionView> {
        private List<transactionStorage> transactions;
        private LayoutInflater inflater;

        public transAdapter(Context c, List<transactionStorage> trans) {
            this.inflater = LayoutInflater.from(c);
            this.transactions = trans;

        }

        @NonNull
        @Override
        public CurrentLoanActivity.transactionView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.transaction_list_item, viewGroup, false);

            return new CurrentLoanActivity.transactionView(viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull CurrentLoanActivity.transactionView transactionView, int i) {
            Log.d("amount", Double.toString(transactions.get(i).calculateAmount()));
            transactionView.tAmount.setText(Double.toString(transactions.get(i).calculateAmount()));
            transactionView.tDate.setText(dateFormat.format(transactions.get(i).getDate()));
            transactionView.tDesc.setText(transactions.get(i).getDescription());
            transactionView.tCategory.setText(transactions.get(transactionView.getAdapterPosition()).getCategory());


        }

        @Override
        public int getItemCount() {
            return this.transactions.size();
        }
    }

    public class transactionView extends RecyclerView.ViewHolder {
        private TextView tAmount;
        private TextView tDesc;
        private EditText tCategory;
        private TextView tDate;


        public transactionView(ViewGroup container) {

            super(LayoutInflater.from(CurrentLoanActivity.this).inflate(R.layout.transaction_list_item, container, false));
            tAmount = (TextView) itemView.findViewById(R.id.txtAmount);
            tDesc = (TextView) itemView.findViewById(R.id.txtDesc);
            tCategory = (EditText) itemView.findViewById(R.id.txtCategory);
            tDate = (TextView) itemView.findViewById(R.id.txtDate);
        }

        public void bind(transactionStorage trans) {
            Log.d("amount", "" + trans.calculateAmount());
            tAmount.setText(Double.toString(trans.calculateAmount()));
            tDate.setText(dateFormat.format(trans.getDate()));
            tDesc.setText(trans.getDescription());
            tCategory.setText(trans.getCategory());
        }

    }
}

