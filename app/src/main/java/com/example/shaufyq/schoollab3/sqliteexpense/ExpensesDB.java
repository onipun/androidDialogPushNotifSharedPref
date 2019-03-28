package com.example.shaufyq.schoollab3.sqliteexpense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.shaufyq.schoollab3.model.ExpenseDBModel;

import java.util.ArrayList;
import java.util.List;

public class ExpensesDB extends SQLiteOpenHelper {

    public static  final String dbName = "dbMyExpense";
    public static  final String tblNameExpense = "expenses";
    public static  final String colExpName = "expenses_name";
    public static  final String colExpPrice = "expense_price";
    public static  final String colExpDate = "expenses_date";
    public static  final String colExpId = "expenses_id";

    public static final String strCrtTblExpenses = "CREATE TABLE "+ tblNameExpense + " ("+ colExpId + " INTEGER PRIMARY KEY, "+ colExpName + " TEXT, "+
            colExpPrice + " REAL,"+ colExpDate + " DATE)";
    public static final String strDropTblExpenses = " DROP TABLE IF EXISTS "+ tblNameExpense;

    public ExpensesDB(Context context){
        super(context,dbName,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(strCrtTblExpenses);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(strDropTblExpenses);
        onCreate(db);
    }

    public float fnInsertExpense(ExpenseDBModel expenseDBModel){
        float retResult = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(colExpName, expenseDBModel.getStrExpName());
        values.put(colExpPrice, expenseDBModel.getStrExpPrice());
        values.put(colExpDate, expenseDBModel.getStrExpDate());

        retResult = db.insert(tblNameExpense, null, values);
        return retResult;
    }

    public ExpenseDBModel fbGetExpenses(int intExpId){
        ExpenseDBModel modelExpenses = new ExpenseDBModel();
        String strSelQry = "SELECT * FROM "+ tblNameExpense + " WHERE "+ colExpId + " = "+ intExpId;
        Cursor cursor = this.getReadableDatabase().rawQuery(strSelQry, null );
        if (cursor != null){
            cursor.moveToFirst();
        }
        modelExpenses.setStrExpPrice(cursor.getDouble(cursor.getColumnIndex(colExpPrice)));
        modelExpenses.setStrExpName(cursor.getString(cursor.getColumnIndex(colExpName)));
        modelExpenses.setStrExpDate(cursor.getString(cursor.getColumnIndex(colExpDate)));
        return modelExpenses;
    }

    public List<ExpenseDBModel> fnGetAllExpenses(){

        List<ExpenseDBModel> listExp = new ArrayList<>();
        String strSelAll = "SELECT * FROM "+ tblNameExpense;
        Cursor cursor = this.getReadableDatabase().rawQuery(strSelAll,null);
        if (cursor.moveToFirst()){
            do{
                ExpenseDBModel modelExpenses = new ExpenseDBModel();
                modelExpenses.setStrExpPrice(cursor.getFloat(cursor.getColumnIndex(colExpPrice)));
                modelExpenses.setStrExpName(cursor.getString(cursor.getColumnIndex(colExpName)));
                modelExpenses.setStrExpDate(cursor.getString(cursor.getColumnIndex(colExpDate)));
                listExp.add(modelExpenses);
            }while (cursor.moveToNext());
        }
        return listExp;
    }
}
