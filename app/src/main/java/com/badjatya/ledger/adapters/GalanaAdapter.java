package com.badjatya.ledger.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badjatya.ledger.R;
import com.badjatya.ledger.Utils.GirviTransactionEvaluator;
import com.badjatya.ledger.activities.GalanaActivity;
import com.badjatya.ledger.activities.ViewGirviTransactionActivity;
import com.badjatya.ledger.holders.GalanaHolder;
import com.badjatya.ledger.models.GirviTransaction;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Tanmay on 9/27/2016.
 */
public class GalanaAdapter extends RecyclerView.Adapter<GalanaHolder>
{
    GalanaActivity context;
    private ArrayList<GirviTransaction> girviTransactionArrayList=new ArrayList<GirviTransaction>();
    GirviTransactionEvaluator girviTransactionEvaluator;
    private static final String EXTRA_GIRVI_TRANSACTION_ID="girvi_transaction_id_intent_extra";
    private static final int REQUEST_CODE_TRANSACTION_DELTED=9;

    public GalanaAdapter(GalanaActivity context,ArrayList<GirviTransaction> girviTransactionArrayList,GirviTransactionEvaluator girviTransactionEvaluator)
    {
        this.context=context;
        this.girviTransactionArrayList=girviTransactionArrayList;
        this.girviTransactionEvaluator=girviTransactionEvaluator;
    }

    @Override
    public GalanaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.list_item_galana,parent,false);
        return new GalanaHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalanaHolder holder, int position) {
       final GirviTransaction gt=girviTransactionArrayList.get(position);
        holder.name.setText(gt.getName());
        holder.fatherName.setText(gt.getFatherName());
        holder.village.setText(gt.getVillage());

        holder.currentByOwed.setText(new DecimalFormat("#.##").format(girviTransactionEvaluator.currentValue(gt))+ "|"
                                        + new DecimalFormat("#.##").format(girviTransactionEvaluator.settleAmount(gt)));

        holder.callingButton.setOnClickListener(new View.OnClickListener() {
            final GalanaHolder fholder = holder;

            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + gt.getPhoneNumber()));
                if (callIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(callIntent);
                }
                else {
                    fholder.callingButton.setEnabled(false);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ViewGirviTransactionActivity.class);
                context.resumeFlag=true;
                intent.putExtra(EXTRA_GIRVI_TRANSACTION_ID,gt.getId());
                context.startActivityForResult(intent,REQUEST_CODE_TRANSACTION_DELTED);
            }
        });
    }

    @Override
    public int getItemCount() {
        return girviTransactionArrayList.size();
    }
}
