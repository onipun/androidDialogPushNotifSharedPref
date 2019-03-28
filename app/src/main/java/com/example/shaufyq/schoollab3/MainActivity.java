package com.example.shaufyq.schoollab3;

import com.example.shaufyq.schoollab3.model.*;
import com.example.shaufyq.schoollab3.sqliteexpense.*;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edtExpname, edtExpPrice, edtExDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtExDate = findViewById(R.id.date);
        edtExpname = findViewById(R.id.name);
        edtExpPrice = findViewById(R.id.value);
    }

    public void fbSave(View view){

        ExpenseDBModel expenseDBModel = new ExpenseDBModel(edtExpname.getText().toString(),Double.parseDouble((edtExpPrice.getText().toString())),edtExDate.getText().toString());
        ExpensesDB expensesDB = new ExpensesDB(getApplicationContext());
        expensesDB.fnInsertExpense(expenseDBModel);

        Toast.makeText(getApplicationContext(), "Expenses Saved!", Toast.LENGTH_SHORT).show();

    }

    public void goToExpensesList(View view){
        Intent intent = new Intent(this, ActvtyExpList.class);
        startActivity(intent);
    }
}
