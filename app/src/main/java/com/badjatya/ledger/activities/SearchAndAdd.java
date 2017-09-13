package com.badjatya.ledger.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.badjatya.ledger.Database.CursorWrappers.CasteCursorWrapper;
import com.badjatya.ledger.Database.CursorWrappers.VillageCursorWrapper;
import com.badjatya.ledger.Database.DatabaseHelper;
import com.badjatya.ledger.Database.InsertAndQueryDb;
import com.badjatya.ledger.Database.ModelToContentValue;
import com.badjatya.ledger.Fragments.VillageNameFragment;
import com.badjatya.ledger.R;
import com.badjatya.ledger.adapters.VillageAdapter;
import com.badjatya.ledger.models.Caste;
import com.badjatya.ledger.models.InterestRates;
import com.badjatya.ledger.models.MetalType;
import com.badjatya.ledger.models.Village;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tanmay on 10/21/2016.
 */
public class SearchAndAdd extends AppCompatActivity implements VillageNameFragment.OnNameRecieved{
    @BindView(R.id.village_search_recycler_view) RecyclerView villageRecyclerView;

    SQLiteDatabase sqLiteDatabase;
    ModelToContentValue modelToContentValue;
    InsertAndQueryDb insertAndQueryDb;
    FragmentManager fm;
    VillageNameFragment vf;
    VillageAdapter villageAdapter;
    private static final int VILLAGES=001;
    private static final int CASTE=002;
    private static final int METALTYPE=003;
    private static final int INTEREST_RATES=004;
    private int logicType;
    List<String> itemList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village_search_add);
        ButterKnife.bind(this);
        logicType=getIntent().getIntExtra("LOGIC", 0);

        sqLiteDatabase= new DatabaseHelper(getApplicationContext()).getWritableDatabase();
        modelToContentValue=new ModelToContentValue();
        insertAndQueryDb =new InsertAndQueryDb(sqLiteDatabase);
        if(logicType==VILLAGES)
        {
            VillageCursorWrapper villageCursorWrapper= insertAndQueryDb.queryVillage(null,null);
            try{
                villageCursorWrapper.moveToFirst();
                while (!villageCursorWrapper.isAfterLast())
                {
                    itemList.add(villageCursorWrapper.getVillage().getVillageName());
                    villageCursorWrapper.moveToNext();
                }
            }
            finally {
                villageCursorWrapper.close();
            }

            vf= VillageNameFragment.newInstance("Village Name:");
        }
        else if(logicType==CASTE)
        {

            CasteCursorWrapper casteCursorWrapper= insertAndQueryDb.queryCaste(null, null);
            try{
                casteCursorWrapper.moveToFirst();
                while (!casteCursorWrapper.isAfterLast())
                {
                    itemList.add(casteCursorWrapper.getCaste().getCasteName());
                    casteCursorWrapper.moveToNext();
                }
            }
            finally {
                casteCursorWrapper.close();
            }
            /*itemList.add("Chatriya");
            itemList.add("Seth");
            itemList.add("Pandit");
            itemList.add("Chamar");*/
            vf= VillageNameFragment.newInstance("Caste Name:");
        }
        else if(logicType==METALTYPE)
        {
            for(int i=0;i<MetalType.getMetalArray().length;i++)
            {
                String []metalType=MetalType.getMetalArray();
                itemList.add(metalType[i]);
            }
        }
        else if(logicType==INTEREST_RATES)
        {
            for(int i=0;i< InterestRates.getRatesArray().length;i++)
            {
                String []rates=InterestRates.getRatesArray();
                itemList.add(rates[i]);
            }
        }
        villageAdapter=new VillageAdapter(this,itemList);
        villageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        villageRecyclerView.setAdapter(villageAdapter);

        fm = getSupportFragmentManager();

    }

    @Override
    public void onNameRecieved(String name) {
        Toast.makeText(getBaseContext(),name,Toast.LENGTH_LONG).show();
        //Logic to put it into the different tables(Village or caste)
        if(logicType==VILLAGES)
        {
            Village village=new Village(name);
            ContentValues values=modelToContentValue.villageToContentValue(village);
            insertAndQueryDb.insertVillage(values);
            itemList.add(name);
            villageAdapter.notifyDataSetChanged();
        }
        else if(logicType==CASTE)
        {
            Caste caste=new Caste(name);
            ContentValues values=modelToContentValue.casteToContentValue(caste);
            insertAndQueryDb.insertCaste(values);
            itemList.add(name);
            villageAdapter.notifyDataSetChanged();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(logicType==VILLAGES||logicType==CASTE)
        {
            inflater.inflate(R.menu.menu_search_and_add_village, menu);
            final MenuItem searchItem = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText)
                {
                    Log.d("ActivitySearch","onQuerytextchange with "+ newText);
                    villageAdapter.getFilter().filter(newText);
                    return true;
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.activity_girvi_add_button)
        {
            vf.show(fm,"village_name_frag");
        }
        return super.onOptionsItemSelected(item);
    }
}
