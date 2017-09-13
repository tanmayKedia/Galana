package com.badjatya.ledger.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.badjatya.ledger.R;

/**
 * Created by Tanmay on 9/27/2016.
 */
public class GirviHolder extends RecyclerView.ViewHolder {

    //TODO link up textviews to the list item(Use butter knife)
    public View itemView;
    public TextView nameTv;
    public TextView fatherNameTv;
    public TextView villageTv;
    public TextView loanAmountTv;
    public TextView interestTv;
    public TextView dateTv;

    public GirviHolder(View itemView)
    {
        super(itemView);
        this.itemView=itemView;
        nameTv=(TextView)itemView.findViewById(R.id.girvi_list_item_name);
        fatherNameTv=(TextView)itemView.findViewById(R.id.girvi_list_item_fathers_name);
        villageTv=(TextView)itemView.findViewById(R.id.girvi_list_item_village_name);
        loanAmountTv=(TextView)itemView.findViewById(R.id.girvi_list_item_lent_amount);
        interestTv=(TextView)itemView.findViewById(R.id.girvi_list_percentage);
        dateTv=(TextView)itemView.findViewById(R.id.girvi_list_date);
    }
}
