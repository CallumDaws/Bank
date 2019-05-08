package com.example.bankingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.gsm.GsmCellLocation;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class transactionPage extends AppCompatActivity {
    //declare varaibles
    public static final String SHARED_PREFS = "SavedData";
    private RecyclerView recyclerView;
    private double balance;
    private String balanceStr, filePath,jsonTrans;;
    transAdapter adapter;
    TextView balanceTxt;
    DateFormat format;
    private ArrayList<transactionStorage> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //get the extras from intents so move data throughout the application.
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            balanceStr = extras.getString("balance");
            if (transactions == null) {
                transactions = (ArrayList<transactionStorage>) extras.getSerializable("transactions");
            }
        }
        //recyclerview delcarations
        recyclerView = (RecyclerView)findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        adapter = new transAdapter(this,transactions);
        recyclerView.setAdapter(adapter);
        balanceTxt = findViewById(R.id.balanceTextView);
        format = new SimpleDateFormat("dd/MM/yyyy");


        balance = Double.valueOf(balanceStr);
        balanceTxt.setText(String.valueOf(balance));
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuIntent = new Intent(transactionPage.this, MenuActivity.class);
                menuIntent.putExtra("transactions",transactions);
                startActivity(menuIntent);
            }
        });

    }

//Adapter for the creation of recyclerview transaction display
    class transAdapter extends RecyclerView.Adapter<transactionView>{
        private List<transactionStorage> transactions;
        private LayoutInflater inflater;

        public transAdapter(Context c, List<transactionStorage> trans) {
            this.inflater = LayoutInflater.from(c);
            this.transactions = trans;

        }

        @NonNull
        @Override
        public transactionView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.transaction_list_item, viewGroup, false);

            return new transactionView(view);
        }

        @Override
        public void onBindViewHolder(@NonNull transactionView transactionView, int i) {
            Log.d("amount", Double.toString(transactions.get(i).calculateAmount()));
            transactionView.tAmount.setText(Double.toString(transactions.get(i).calculateAmount()));
            transactionView.tDate.setText(format.format(transactions.get(i).getDate()));
            transactionView.tDesc.setText(transactions.get(i).getDescription());
            transactionView.tCategory.setTag(i);
            transactionView.tCategory.setText(transactions.get(transactionView.getAdapterPosition()).getCategory());


        }

        @Override
        public int getItemCount() {
            return this.transactions.size();
        }
    }
    //ViewHolder for transactions.
    public class transactionView extends RecyclerView.ViewHolder {
        private TextView tAmount;
        private TextView tDesc;
        private EditText tCategory;
        private TextView tDate;


        public transactionView(View container){
        //declare variables
            super(container);
            tAmount = (TextView) itemView.findViewById(R.id.txtAmount);
            tDesc = (TextView) itemView.findViewById(R.id.txtDesc);
            tCategory = itemView.findViewById(R.id.txtCategory);
            tDate = (TextView) itemView.findViewById(R.id.txtDate);
            EditTextListener textListener = new EditTextListener(tCategory);
            tCategory.addTextChangedListener(textListener);
        }
        public void bind(transactionStorage trans){
            Log.d("amount", "" + trans.calculateAmount());
            tAmount.setText(Double.toString(trans.calculateAmount()));
            tDate.setText(format.format(trans.getDate()));
            tDesc.setText(trans.getDescription());
            tCategory.setText(trans.getCategory());
        }

    }
    //TextListener for edittexct to change the category of each transaction
    private class EditTextListener implements TextWatcher{
        private int position;
        private EditText editText;

        public void updatePosition(int position){
            this.position = position;
        }

        private EditTextListener(EditText editText){
            this.editText = editText;

        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3){

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3){
        }

        @Override
        public void afterTextChanged(Editable editable){
            int position = (int) editText.getTag();
            transactions.get(position).setCategory(editable.toString());

        }
    }

  /*  protected void onPause(){
        super.onPause();

        SharedPreferences settings = PreferenceManager .getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
         Gson gson = new Gson();
        editor.clear();
      jsonTrans = gson.toJson(transactions);
      editor.putString("transactionJson",jsonTrans);
      editor.commit();

    }

    protected void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        Type type = new TypeToken<List<transactionStorage>>(){}.getType();
        jsonTrans = sharedPreferences.getString("transactionJson", "");
        transactions = gson.fromJson(jsonTrans,type);
    } */
}

