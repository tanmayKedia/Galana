package com.badjatya.ledger.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badjatya.ledger.Fragments.AlertFragment;
import com.badjatya.ledger.R;
import com.badjatya.ledger.Utils.TimeConverter;
import com.badjatya.ledger.holders.TransactionHolder;
import com.badjatya.ledger.models.Transaction;

import java.util.ArrayList;

/**
 * Created by Tanmay on 10/1/2016.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionHolder> {

    Context context;
    FragmentManager fragmentManager;
    private ArrayList<Transaction> transactionsList;
    public String deleteTransactionId;
    public int settled=0;
    private static final int BUTTON_CODE_DELETE_BUTTON=001;

    public TransactionAdapter(Context context,FragmentManager fragmentManager,ArrayList<Transaction> transactionsList)
    {
        this.context=context;
        this.fragmentManager=fragmentManager;
        this.transactionsList=transactionsList;
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.list_item_transaction,parent,false);
        return new TransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionHolder holder, final int position) {
        holder.transactionDate.setText(TimeConverter.convertEpochToDateString(transactionsList.get(position).getDate()));

        if(new Double(transactionsList.get(position).getAmount())<0)
        {
            holder.transactionAmount.setTextColor(context.getResources().getColor(R.color.transaparentRed));
            holder.transactionAmount.setText("Rs. " + -1 * new Double(transactionsList.get(position).getAmount()));
            holder.paidOnText.setText(" taken on ");
        }
        else
        {
            holder.transactionAmount.setTextColor(context.getResources().getColor(R.color.Green));
            holder.transactionAmount.setText("Rs. " + new Double(transactionsList.get(position).getAmount()));
            holder.paidOnText.setText(" paid on ");

        }
        if(settled==3||settled==2)
        {
            holder.deleteButton.setEnabled(false);
            return;
        }
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTransactionId=transactionsList.get(position).getId();
                AlertFragment.newInstance("Are you sure you want to delete this transaction?",BUTTON_CODE_DELETE_BUTTON)
                        .show(fragmentManager,"d1");
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }
}
