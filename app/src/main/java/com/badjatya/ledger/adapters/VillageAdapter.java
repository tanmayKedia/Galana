package com.badjatya.ledger.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.badjatya.ledger.R;
import com.badjatya.ledger.activities.SearchAndAdd;
import com.badjatya.ledger.holders.VillageHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanmay on 10/22/2016.
 */
public class VillageAdapter extends RecyclerView.Adapter<VillageHolder> implements Filterable{

    Context context;
    SearchAndAdd activity;
    private List<String> list;
    private List<String> villageNameListOriginal;

    public VillageAdapter(SearchAndAdd activity,List<String> villageNameList)
    {
        this.context=activity.getBaseContext();
        this.activity=activity;
        this.villageNameListOriginal =villageNameList;
        this.list =villageNameList;
    }

    @Override
    public VillageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.list_item_village_search,parent,false);
        return new VillageHolder(view);
    }

    @Override
    public void onBindViewHolder(final VillageHolder holder, final int position) {
        holder.villageName.setText(list.get(position));
        holder.wholeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data=new Intent();
                data.putExtra("NAME",holder.villageName.getText().toString().trim());
                activity.setResult(Activity.RESULT_OK,data);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<String> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = villageNameListOriginal;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<String>) results.values;
                Log.d("ActivitySearch", "before notify data set changed");
                VillageAdapter.this.notifyDataSetChanged();
                Log.d("ActivitySearch", "after ndsc");
            }
        };
    }

    protected List<String> getFilteredResults(String constraint) {
        List<String> results = new ArrayList<>();

        for (String item : villageNameListOriginal) {
            if (item.toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

}
