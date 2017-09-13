package com.badjatya.ledger.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.badjatya.ledger.R;


/**
 * Created by Tanmay on 10/22/2016.
 */
public class VillageHolder extends RecyclerView.ViewHolder
{
    public TextView villageName;
    public View wholeView;

    public VillageHolder(View itemView)
    {
        super(itemView);
        wholeView=itemView;
        villageName=(TextView)itemView.findViewById(R.id.village_list_item_name);
    }
}
