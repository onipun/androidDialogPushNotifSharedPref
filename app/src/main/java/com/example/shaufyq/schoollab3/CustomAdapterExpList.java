package com.example.shaufyq.schoollab3;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shaufyq.schoollab3.model.ExpenseDBModel;

import java.util.List;

public class CustomAdapterExpList extends RecyclerView.Adapter<CustomAdapterExpList.ViewHolder> {

    List<ExpenseDBModel> listExpenses;

    public CustomAdapterExpList(List<ExpenseDBModel> expenseDBModels){
        this.listExpenses = expenseDBModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_expenses_recyler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterExpList.ViewHolder holder, int position){
        ExpenseDBModel expenseDBModel = listExpenses.get(position);
        holder.txtVwExpName.setText(expenseDBModel.getStrExpName());
        holder.txtVwExpPrice.setText(String.valueOf(expenseDBModel.getStrExpPrice()));
        holder.txtVwExpDate.setText(expenseDBModel.getStrExpDate());
    }

    @Override
    public int getItemCount(){
        return listExpenses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtVwExpName, txtVwExpPrice, txtVwExpDate;
        public ViewHolder(View itemView){
            super(itemView);
            txtVwExpName = itemView.findViewById(R.id.txtVwExpName);
            txtVwExpPrice = itemView.findViewById(R.id.txtVwExpPrice);
            txtVwExpDate = itemView.findViewById(R.id.txtVwExpDate);
        }
    }


}
