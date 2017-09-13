package com.badjatya.ledger.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.badjatya.ledger.R;

/**
 * Created by Tanmay on 9/27/2016.
 */
public class GalanaHolder extends RecyclerView.ViewHolder
{
    //TODO link up textviews to the list item(Use butter knife)
    public View itemView;
    public TextView name;
    public TextView fatherName;
    public TextView village;
    public ImageButton callingButton;
    public TextView currentByOwed;
    public GalanaHolder(View itemView)
    {
        super(itemView);
        this.itemView=itemView;
        name=(TextView)itemView.findViewById(R.id.galana_list_item_name);
        fatherName=(TextView)itemView.findViewById(R.id.galana_list_item_fathers_name);
        village=(TextView)itemView.findViewById(R.id.galana_list_item_village_name);
        currentByOwed=(TextView)itemView.findViewById(R.id.galana_list_item_current_by_owed);
        callingButton=(ImageButton)itemView.findViewById(R.id.galana_list_item_call_button);
    }
}