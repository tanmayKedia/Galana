package com.badjatya.ledger.Rakam.RakamList;

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

import com.badjatya.ledger.Database.CursorWrappers.RakamTransactionCursorWrapper;
import com.badjatya.ledger.Database.DatabaseHelper;
import com.badjatya.ledger.Database.InsertAndQueryDb;
import com.badjatya.ledger.R;
import com.badjatya.ledger.Rakam.AddRakam.AddRakamActivity;
import com.badjatya.ledger.Rakam.RakamTransaction;
import com.badjatya.ledger.activities.FilterActivity;
import com.badjatya.ledger.models.GirviFilter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tanmay on 11/28/2016.
 */
public class RakamActivity extends AppCompatActivity {

    @BindView(R.id.girvi_recycler_view)
    RecyclerView rakamRecyclerView;
    @BindView(R.id.girvi_filter_floating_button)
    FloatingActionButton floatingFilterButton;
    private static final int REQUEST_CODE_ADD_NEW_RAKAM_TRANSACTION =001;
    private static final int REQUEST_CODE_FILTER_ACTIVITY=002;
    private static final int REQUEST_CODE_TRANSACTION_DELTED=9;

    SQLiteDatabase sqLiteDatabase;
    InsertAndQueryDb insertAndQueryDb;
    ArrayList<RakamTransaction> originalTransactionList=new ArrayList<RakamTransaction>();
    ArrayList<RakamTransaction> filteredTransactionList=new ArrayList<RakamTransaction>();
    RakamAdapter rakamAdapter;
    GirviFilter girviFilter=new GirviFilter();
    RakamTransactionFilter rakamTransactionFilter=new RakamTransactionFilter();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girvi);
        ButterKnife.bind(this);
        sqLiteDatabase = new DatabaseHelper(this).getWritableDatabase();
        insertAndQueryDb = new InsertAndQueryDb(sqLiteDatabase);
        fetchRakamTransactions(null,null);
        rakamAdapter=new RakamAdapter(originalTransactionList,this);
        rakamRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rakamRecyclerView.setAdapter(rakamAdapter);
        setFloatingButtonIcon(girviFilter);
        floatingFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(), FilterActivity.class);
                intent.putExtra("filter",girviFilter);
                startActivityForResult(intent,REQUEST_CODE_FILTER_ACTIVITY);
            }
        });
    }

    private void fetchRakamTransactions(String where, String[] whereArgs) {
        RakamTransactionCursorWrapper rcw = insertAndQueryDb.queryRakamTransactionTable(where, whereArgs);

        try {
            rcw.moveToFirst();
            while (!rcw.isAfterLast()) {
                originalTransactionList.add(rcw.getRakamTransaction());
                rcw.moveToNext();
            }
        }
        finally {
            rcw.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_ADD_NEW_RAKAM_TRANSACTION) {
            originalTransactionList.clear();
            fetchRakamTransactions(null, null);
            filteredTransactionList=rakamTransactionFilter.filterRakamTransactions(girviFilter,originalTransactionList);
            swapAdapter(filteredTransactionList);
        }
        if(requestCode==REQUEST_CODE_FILTER_ACTIVITY)
        {
            girviFilter=(GirviFilter)data.getSerializableExtra("filter");
            setFloatingButtonIcon(girviFilter);
            filteredTransactionList=rakamTransactionFilter.filterRakamTransactions(girviFilter,originalTransactionList);
            swapAdapter(filteredTransactionList);
        }
        if(requestCode==REQUEST_CODE_TRANSACTION_DELTED)
        {
            originalTransactionList.clear();
            fetchRakamTransactions(null, null);
            filteredTransactionList=rakamTransactionFilter.filterRakamTransactions(girviFilter,originalTransactionList);
            swapAdapter(filteredTransactionList);
        }
    }

    public void swapAdapter(ArrayList<RakamTransaction> newList)
    {
        rakamAdapter=new RakamAdapter(newList,this);
        rakamRecyclerView.swapAdapter(rakamAdapter,true);
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
            Intent intent=new Intent(getBaseContext(), AddRakamActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_NEW_RAKAM_TRANSACTION);
        }
        return super.onOptionsItemSelected(item);
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
