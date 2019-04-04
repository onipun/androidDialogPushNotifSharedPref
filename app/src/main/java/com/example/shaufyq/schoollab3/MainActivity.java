package com.example.shaufyq.schoollab3;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shaufyq.schoollab3.model.*;
import com.example.shaufyq.schoollab3.sqliteexpense.*;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText edtExpname, edtExpPrice, edtExDate, edtExpTime;
    String strURL = "http://127.0.0.1/webServiceJSON.php";
    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtExDate = findViewById(R.id.date);
        edtExpname = findViewById(R.id.name);
        edtExpTime = findViewById(R.id.time);
        edtExpPrice = findViewById(R.id.value);


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, strURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            edtExDate.setText(jsonObject.getString("currDate"));
                            edtExpTime.setText(jsonObject.getString("currTime"));
                            Log.d(TAG, "onResponse: " + jsonObject.getString("currTime"));
                        } catch (Exception ee) {
                            Log.e(TAG, "onResponse: ", ee);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error );
            }
        }){
          protected Map<String, String> getParams() throws AuthFailureError{
              Map<String, String> params = new HashMap<String, String>();
              params.put("selectFn", "fnGetDateTime");
              return params;
          }
        };

        requestQueue.add(stringRequest);

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
