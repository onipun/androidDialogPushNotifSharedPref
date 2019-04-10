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

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.security.PublicKey;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtExpname, edtExpPrice, edtExDate, edtExpTime;
    private int mYear,mMonth,mDay,mHour,mMinute;

    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edtExDate = findViewById(R.id.date);
        edtExpname = findViewById(R.id.name);
        edtExpTime = findViewById(R.id.edtTime);
        edtExpPrice = findViewById(R.id.value);

        edtExDate.setOnClickListener(this);
        edtExpTime.setOnClickListener(this);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="http://192.168.43.173/webServiceJSON/globalServiceJSON.php";
        String url ="http://localhost/webServiceJSON/globalServiceJSON.php";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            edtExDate.setText(jsonObject.getString("currDate"));
                            edtExpTime.setText(jsonObject.getString("currTime"));
                        }catch (Exception e){
                            Log.e(TAG, "onResponse: ", e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: That didn't work!");
                Log.e(TAG, "onErrorResponse: ",error );
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("selectFn","fnGetDateTime");
                return params;
            }

        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }




    public void fbSave(View view){




        final ExpenseDBModel expenseDBModel = new ExpenseDBModel(edtExpname.getText().toString(),
                Double.parseDouble(edtExpPrice.getText().toString()),edtExDate.getText().toString());

        ExpensesDB expensesDB = new ExpensesDB(getApplicationContext());
        expensesDB.fnInsertExpense(expenseDBModel);

        Log.d(TAG, "fbSave: " + expenseDBModel.getStrExpName() + String.valueOf(expenseDBModel.getStrExpPrice()) + expenseDBModel.getStrExpDate());

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="http://192.168.43.173/webServiceJSON/globalServiceJSON.php";
        String url ="http://localhost/webServiceJSON/globalServiceJSON.php";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: That didn't work!");
                Log.e(TAG, "onErrorResponse: ",error );
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("selectFn","fnAddExpense");
                params.put("varExpName",expenseDBModel.getStrExpName());
                params.put("varExpPrice", String.valueOf(expenseDBModel.getStrExpPrice()));
                params.put("varMobileDate",expenseDBModel.getStrExpDate());
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        Toast.makeText(getApplicationContext(),"expenses Saved!",Toast.LENGTH_SHORT).show();



    }

    public void goToExpensesList(View view){
        Intent intent = new Intent(this, ActvtyExpList.class);
        startActivity(intent);
    }

    public void sendNotification(View view){
        // 1. Define the NotificationChannel
        NotificationChannel noEmpty = new NotificationChannel("DailyExpenses", "Daily Expenses",
                NotificationManager.IMPORTANCE_DEFAULT);

        //2. Set Which activity that will opened when user click on the notificaiton.
        Intent noIntent = new Intent(getApplicationContext(), MainActivity.class);

        //3. Add the intent into TaskStackBuilder
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(noIntent);

        //4. Define PendingIntent
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        ///5. Define and call NotificationManager that the app is going to perform push Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "DailyExpenses");
        builder.setContentText("Just want to notify you!").setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true);


        // 6. Define and call NotificationManager that the app is foing to perform push Notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE){
            manager.createNotificationChannel(noEmpty);
        }
        manager.notify(0,builder.build());

    }

    //1. Declare as class members
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public void sharedPrefereance(){

        //2. Initialize inside onCreate(), Name of file should be same through all acttivities
        sharedPreferences = getApplicationContext().getSharedPreferences("DailyExpenses", 0);
        editor = sharedPreferences.edit();

        //3. Set the key and value using putString() and then commit()
        editor.putString("username", edtExpname.getText().toString());
        editor.commit();

        //4. In other activity, repaeat step 1 and 2 to make changes
        //5. Use getString() for retrieving value from the stored file
        String username = sharedPreferences.getString("username", null);
        Toast.makeText(getApplicationContext(),"Welcome "+ username,Toast.LENGTH_LONG).show();
    }

    public void showDialog(View view){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Confirm to save?");
        alertBuilder.setPositiveButton("Agreed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //proceed to perform logical process
            }
        });

        alertBuilder.setNegativeButton("No Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Logical process if user cancel or decline
            }
        });
        alertBuilder.show();
    }

    @Override
    public void onClick(View v) {
        if (v == edtExDate){

            //Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            edtExDate.setText(dayOfMonth + "-" + (month) + "-" + year);
                        }
                    }, mYear,mMonth,mDay);
            datePickerDialog.show();
        }else if (v == edtExpTime){

            //Get Current TIme
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            //launch tinme Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    edtExpTime.setText(hourOfDay + "-" + minute );
                }
            }, mHour,mMinute,false);
            timePickerDialog.show();
        }
    }
}
