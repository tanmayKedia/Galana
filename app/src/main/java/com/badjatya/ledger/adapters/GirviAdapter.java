package com.badjatya.ledger.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badjatya.ledger.R;
import com.badjatya.ledger.Utils.TimeConverter;
import com.badjatya.ledger.activities.GirviActivity;
import com.badjatya.ledger.activities.ViewGirviTransactionActivity;
import com.badjatya.ledger.holders.GirviHolder;
import com.badjatya.ledger.models.GirviTransaction;
import com.badjatya.ledger.models.InterestRates;

import java.util.ArrayList;

/**
 * Created by Tanmay on 9/27/2016.
 */
public class GirviAdapter extends RecyclerView.Adapter<GirviHolder> {

    GirviActivity context;
    private ArrayList<GirviTransaction> girviTransactionsList;
    private static final String EXTRA_GIRVI_TRANSACTION_ID="girvi_transaction_id_intent_extra";
    private static final int REQUEST_CODE_TRANSACTION_DELTED=9;

    public GirviAdapter(GirviActivity context,ArrayList<GirviTransaction> girviTransactionsList)
    {
        this.context=context;
        this.girviTransactionsList=girviTransactionsList;
    }

    @Override
    public GirviHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.list_item_girvi,parent,false);
        return new GirviHolder(view);
    }

    @Override
    public void onBindViewHolder(GirviHolder holder, int position) {
        final GirviTransaction girviTransaction=girviTransactionsList.get(position);
        holder.nameTv.setText(girviTransaction.getName());
        holder.fatherNameTv.setText(girviTransaction.getFatherName());
        holder.villageTv.setText(girviTransaction.getVillage());
        holder.loanAmountTv.setText(new Double(girviTransaction.getMoneyGiven()).toString());
        holder.interestTv.setText(InterestRates.getRateFormIndex(girviTransaction.getInterestPercentage()));
        holder.dateTv.setText("on "+ TimeConverter.convertEpochToDateString(girviTransaction.getDate()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ViewGirviTransactionActivity.class);
                intent.putExtra(EXTRA_GIRVI_TRANSACTION_ID,girviTransaction.getId());
                context.startActivityForResult(intent,REQUEST_CODE_TRANSACTION_DELTED);
            }
        });

    }

    @Override
    public int getItemCount() {
        return girviTransactionsList.size();
    }
}
