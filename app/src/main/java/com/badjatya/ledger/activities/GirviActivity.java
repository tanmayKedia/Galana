package com.badjatya.ledger.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.badjatya.ledger.Database.CursorWrappers.GirviTransactionCursorWrapper;
import com.badjatya.ledger.Database.DatabaseHelper;
import com.badjatya.ledger.Database.InsertAndQueryDb;
import com.badjatya.ledger.Utils.GirviTransactionFilter;
import com.badjatya.ledger.R;
import com.badjatya.ledger.adapters.GirviAdapter;
import com.badjatya.ledger.models.GirviFilter;
import com.badjatya.ledger.models.GirviTransaction;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tanmay on 9/27/2016.
 */
public class GirviActivity extends AppCompatActivity {

    @BindView(R.id.girvi_recycler_view)
    RecyclerView girviRecyclerView;
    @BindView(R.id.girvi_filter_floating_button)
    FloatingActionButton floatingFilterButton;
    ArrayList<GirviTransaction> originalTransactionList = new ArrayList<GirviTransaction>();
    ArrayList<GirviTransaction> girviTransactionsList = new ArrayList<GirviTransaction>();
    private static final int REQUEST_CODE_ADD_GIRVI_TRANSACTION = 001;
    private static final int REQUEST_CODE_FILTER = 002;
    private static final int REQUEST_CODE_TRANSACTION_DELTED=9;
    SQLiteDatabase sqLiteDatabase;
    InsertAndQueryDb insertAndQueryDb;
    GirviAdapter girviAdapter;
    GirviFilter girviFilter=new GirviFilter();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girvi);
        ButterKnife.bind(this);
        sqLiteDatabase = new DatabaseHelper(this).getWritableDatabase();
        insertAndQueryDb = new InsertAndQueryDb(sqLiteDatabase);
        fetchGirviTransactions(null, null);
        girviTransactionsList=(ArrayList)originalTransactionList.clone();
        girviAdapter = new GirviAdapter(this, girviTransactionsList);
        girviRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        girviRecyclerView.setAdapter(girviAdapter);

        floatingFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FilterActivity.class);
                intent.putExtra("filter",girviFilter);
                startActivityForResult(intent, REQUEST_CODE_FILTER);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_girvi_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.activity_girvi_add_button)
        {
            Intent intent = new Intent(this, AddGirviTransactionActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_GIRVI_TRANSACTION);
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchGirviTransactions(String where, String[] whereArgs) {
        GirviTransactionCursorWrapper gcw = insertAndQueryDb.queryGirviTransactionTable(where, whereArgs);

        try {
            gcw.moveToFirst();
            while (!gcw.isAfterLast()) {
                originalTransactionList.add(gcw.getGirviTransaction());
                gcw.moveToNext();
            }
        } finally {
            gcw.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_ADD_GIRVI_TRANSACTION)
        {
            girviTransactionsList.clear();
            originalTransactionList.clear();
            fetchGirviTransactions(null, null);
            applyFilter(girviFilter);
        }
        if (requestCode == REQUEST_CODE_FILTER)
        {
            girviFilter = (GirviFilter) data.getSerializableExtra("filter");
            applyFilter(girviFilter);
        }
        if(requestCode==REQUEST_CODE_TRANSACTION_DELTED)
        {
            girviTransactionsList.clear();
            originalTransactionList.clear();
            fetchGirviTransactions(null, null);
            applyFilter(girviFilter);
        }
    }
    private void applyFilter(GirviFilter girviFilter)
    {
        GirviTransactionFilter gtf=new GirviTransactionFilter();
        girviTransactionsList=gtf.filterGirviTransactions(girviFilter,originalTransactionList);
        changeAdapterWithNewListInstance(girviTransactionsList);
        setFloatingButtonIcon(girviFilter);
    }
    private void changeAdapterWithNewListInstance(ArrayList<GirviTransaction> girviTransactionsList)
    {
        girviAdapter=new GirviAdapter(this,girviTransactionsList);
        girviRecyclerView.swapAdapter(girviAdapter,true);
    }

    private void setFloatingButtonIcon(GirviFilter girviFilter)
    {   int count=0;
        //this works because if the first condition is false it will not execute the second hence we wont get null pointer exception
        if(girviFilter.getName()!=null&&girviFilter.getName().length()>0)
        {
            count++;
        }
        if(girviFilter.getFatherName()!=null&&girviFilter.getFatherName().length()>0)
        {
            count++;
        }
        if(girviFilter.getVillage()!=null&&girviFilter.getVillage().length()>0)
        {
            count++;
        }
        if(girviFilter.getCaste()!=null&&girviFilter.getCaste().length()>0)
        {
            count++;
        }

        switch (count)
        {
            case 0:
                floatingFilterButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_list_white_24dp));
            break;
            case 1:
                floatingFilterButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_1_white_24dp));
            break;
            case 2:
                floatingFilterButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_2_white_24dp));
            break;
            case 3:
                floatingFilterButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_3_white_24dp));
            break;
            case 4:
                floatingFilterButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_filter_4_white_24dp));
            break;
        }
    }



}