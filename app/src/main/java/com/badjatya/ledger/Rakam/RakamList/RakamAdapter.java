package com.badjatya.ledger.Rakam.RakamList;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badjatya.ledger.R;
import com.badjatya.ledger.Rakam.RakamTransaction;
import com.badjatya.ledger.Rakam.ViewRakam.ViewRakamTransaction;
import com.badjatya.ledger.Utils.TimeConverter;
import com.badjatya.ledger.activities.ViewGirviTransactionActivity;
import com.badjatya.ledger.holders.GirviHolder;
import com.badjatya.ledger.models.GirviTransaction;
import com.badjatya.ledger.models.InterestRates;

import java.util.ArrayList;

/**
 * Created by Tanmay on 11/29/2016.
 */
public class RakamAdapter extends RecyclerView.Adapter<RakamTransactionHolder> {
    ArrayList<RakamTransaction> rakamTransactionArrayList=new ArrayList<RakamTransaction>();
    private static final String EXTRA_RAKAM_TRANSACTION_ID="rakam_transaction_id_intent_extra";
    RakamActivity context;
    private static final int REQUEST_CODE_TRANSACTION_DELTED=9;

    public RakamAdapter(ArrayList<RakamTransaction> rakamTransactionArrayList,RakamActivity context)
    {
        this.rakamTransactionArrayList=rakamTransactionArrayList;
        this.context=context;

    }

    @Override
    public RakamTransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.list_item_girvi,parent,false);
        return new RakamTransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(RakamTransactionHolder holder, int position) {
        final RakamTransaction rakamTransaction=rakamTransactionArrayList.get(position);
        holder.nameTv.setText(rakamTransaction.getName());
        holder.fatherNameTv.setText(rakamTransaction.getFatherName());
        holder.villageTv.setText(rakamTransaction.getVillage());
        holder.loanAmountTv.setText(new Double(rakamTransaction.getMoneyGiven()).toString());
        holder.interestTv.setText(InterestRates.getRateFormIndex(rakamTransaction.getInterestPercentage()));
        holder.dateTv.setText("on "+ TimeConverter.convertEpochToDateString(rakamTransaction.getDate()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(context, ViewRakamTransaction.class);
                intent.putExtra(EXTRA_RAKAM_TRANSACTION_ID,rakamTransaction.getId());
                context.startActivityForResult(intent,REQUEST_CODE_TRANSACTION_DELTED);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return rakamTransactionArrayList.size();
    }
}
