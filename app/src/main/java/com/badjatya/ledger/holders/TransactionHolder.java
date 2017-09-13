package com.badjatya.ledger.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.badjatya.ledger.R;

/**
 * Created by Tanmay on 10/1/2016.
 */
public class TransactionHolder extends RecyclerView.ViewHolder
{
    public TextView transactionAmount;
    public TextView transactionDate;
    public ImageButton deleteButton;
    public TextView paidOnText;
    public TransactionHolder(View itemView)
    {
        super(itemView);
        deleteButton=(ImageButton)itemView.findViewById(R.id.list_item_transaction_delete_button);
        transactionAmount=(TextView)itemView.findViewById(R.id.list_item_transaction_amount);
        transactionDate=(TextView) itemView.findViewById(R.id.list_item_transaction_date);
        paidOnText=(TextView)itemView.findViewById(R.id.list_item_transaction_paid_on);
    }
}
