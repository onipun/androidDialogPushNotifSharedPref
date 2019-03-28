package com.example.shaufyq.schoollab3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.shaufyq.schoollab3.model.ExpenseDBModel;
import com.example.shaufyq.schoollab3.sqliteexpense.ExpensesDB;

import java.util.ArrayList;

public class ActvtyExpList extends AppCompatActivity {

    RecyclerView recyclerViewExpList;
    ArrayList<ExpenseDBModel> expensesList;
    ExpensesDB expensesDB;
    CustomAdapterExpList customAdapterExpList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actvty_exp_list);

        recyclerViewExpList = findViewById(R.id.recyListExp);
        expensesDB = new ExpensesDB(getApplicationContext());
        expensesList = (ArrayList<ExpenseDBModel>) expensesDB.fnGetAllExpenses();
        customAdapterExpList = new CustomAdapterExpList(expensesDB.fnGetAllExpenses());

        recyclerViewExpList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewExpList.setAdapter(customAdapterExpList);
        customAdapterExpList.notifyDataSetChanged();
    }
}
