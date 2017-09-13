package com.badjatya.ledger.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.badjatya.ledger.Database.CursorWrappers.GirviTransactionCursorWrapper;
import com.badjatya.ledger.Database.DatabaseHelper;
import com.badjatya.ledger.Database.InsertAndQueryDb;
import com.badjatya.ledger.Fragments.RateFragment;
import com.badjatya.ledger.Listeners.DrawerItemClickListener;
import com.badjatya.ledger.R;
import com.badjatya.ledger.Utils.GirviTransactionEvaluator;
import com.badjatya.ledger.Utils.GirviTransactionFilter;
import com.badjatya.ledger.Utils.TimeConverter;
import com.badjatya.ledger.adapters.GalanaAdapter;
import com.badjatya.ledger.models.GirviFilter;
import com.badjatya.ledger.models.GirviTransaction;
import com.badjatya.ledger.Database.DatabaseSchema.GirviTransactionTable;

import org.joda.time.LocalDate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tanmay on 9/25/2016.
 */
public class GalanaActivity extends AppCompatActivity implements RateFragment.OnRatesRecieved
{
    @BindView(R.id.galana_recycler_view) RecyclerView galanaRecyclerView;
    @Nullable@BindView(R.id.drawer_listview) ListView drawer;
    @BindView(R.id.navigation_drawer)DrawerLayout drawerLayout ;
    @BindView(R.id.galana_edit_rate_button)ImageButton editRateButton;
    @BindView(R.id.galana_gold_price_textView)TextView goldPriceTextView;
    @BindView(R.id.galana_silver_pice_textview)TextView silverPriceTextView;
    @BindView(R.id._galana_girvi_filter_floating_button)
    FloatingActionButton floatingFilterButton;
    private ActionBarDrawerToggle toggle;
    private FragmentManager fm;
    private RateFragment rateFragment;
    private static final String PREF_GOLD_RATE="gold_rate";
    private static final String PREF_SILVER_RATE="silver_rate";
    public boolean resumeFlag=false;
    private static final int REQUEST_CODE_FILTER = 002;
    private static final int REQUEST_CODE_PICK_DB=003;
    private static final int REQUEST_CODE_TRANSACTION_DELTED=9;
    SharedPreferences pref;
    private ArrayList<GirviTransaction> originalList=new ArrayList<GirviTransaction>();
    ArrayList<GirviTransaction> girviTransactionsList = new ArrayList<GirviTransaction>();
    SQLiteDatabase sqLiteDatabase;
    InsertAndQueryDb insertAndQueryDb;
    GalanaAdapter galanaAdapter;
    GirviFilter girviFilter=new GirviFilter();
    GirviTransactionEvaluator girviTransactionEvaluator;
    String TAG="fileVoldemort";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galana_drawer);
        ButterKnife.bind(this);
        pref =PreferenceManager.getDefaultSharedPreferences(this);
        girviTransactionEvaluator=new GirviTransactionEvaluator(this);
        setUpDrawer();
        fm = getSupportFragmentManager();
        rateFragment= RateFragment.newInstance("Set Rates");
        setupPrices();
        sqLiteDatabase= new DatabaseHelper(this).getWritableDatabase();
        insertAndQueryDb =new InsertAndQueryDb(sqLiteDatabase);

        if (areRatesSet())
        {
            loadQualifiedGirviTransactions();
        }
        else
        {
            Toast.makeText(this,"Enter Rates To Check Items For Melting!",Toast.LENGTH_LONG).show();
        }
        galanaAdapter=new GalanaAdapter(this,originalList,girviTransactionEvaluator);
        galanaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        galanaRecyclerView.setAdapter(galanaAdapter);

        floatingFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FilterActivity.class);
                intent.putExtra("filter", girviFilter);
                startActivityForResult(intent, REQUEST_CODE_FILTER);
            }
        });




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_FILTER)
        {
            girviFilter = (GirviFilter) data.getSerializableExtra("filter");
            applyFilter(girviFilter);
        }

        if (requestCode == REQUEST_CODE_PICK_DB
                && resultCode == Activity.RESULT_OK) {
            Uri dbUri = data.getData();
            Log.d(TAG, "Db URI= " + dbUri.getPath());


            File backupDbFile = new File(dbUri.getPath());
            FileInputStream fis=null;
            OutputStream output=null;
            try {

                fis = new FileInputStream(backupDbFile);
                String internalDbFile = getDatabasePath("galana.db").getPath();
                output = new FileOutputStream(internalDbFile,false);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer))>0){
                    output.write(buffer, 0, length);
                }
            }
            catch (Exception e)
            {
                Log.d(TAG, e.toString());
                Toast.makeText(this,"There was error accessing the database file",Toast.LENGTH_LONG).show();
            }
            finally {
                // Close the streams
                try
                {
                    if(fis!=null)
                    {
                        fis.close();
                    }
                    if(output!=null)
                    {
                        output.flush();
                        output.close();
                    }
                }
                catch (IOException ioe)
                {
                    Log.d("Voldemort", ioe.toString());
                }
            }

        }
        if(requestCode==REQUEST_CODE_TRANSACTION_DELTED)
        {
            loadQualifiedGirviTransactions();
            applyFilter(girviFilter);
        }
    }

    private void applyFilter(GirviFilter girviFilter)
    {
        GirviTransactionFilter gtf=new GirviTransactionFilter();
        girviTransactionsList=gtf.filterGirviTransactions(girviFilter,originalList);
        changeAdapterWithNewListInstance(girviTransactionsList);
        setFloatingButtonIcon(girviFilter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(resumeFlag==true)
        {
            resumeFlag=false;
            loadQualifiedGirviTransactions();
            applyFilter(girviFilter);

        }

    }

    private boolean areRatesSet() {
        Double goldRate = Double.longBitsToDouble(pref.getLong(PREF_GOLD_RATE, 0));
        Double silverRate=Double.longBitsToDouble(pref.getLong(PREF_SILVER_RATE,0));
        setPricesTextView(goldRate, silverRate);
        if(goldRate==0||silverRate==0)
        {
            return false;
        }
        return true;
    }

    private void loadQualifiedGirviTransactions() {
        originalList.clear();

        ArrayList<GirviTransaction> localList=new ArrayList<GirviTransaction>();
        GirviTransactionCursorWrapper gcw=insertAndQueryDb.queryGirviTransactionTable(
                GirviTransactionTable.Cols.SETTLE_STATUS + "=1"
                ,
                null
        );
        try {
            gcw.moveToFirst();
            while (!gcw.isAfterLast()) {
                //Toast.makeText(this, "inside while", Toast.LENGTH_SHORT).show();
                localList.add(gcw.getGirviTransaction());
                Log.d("voldemort", gcw.getGirviTransaction().toString());
                gcw.moveToNext();
            }
        } finally {
            gcw.close();
        }
        if(localList.size()>0)
        {
            for(GirviTransaction gt:localList)
            {
                if(girviTransactionEvaluator.isReadyToMelt(gt))
                {
                    originalList.add(gt);
                    Log.d("voldemort","is ready to melt"+gt.toString());
                }
            }
        }
    }

    private void setupPrices() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Double goldRate = Double.longBitsToDouble(pref.getLong(PREF_GOLD_RATE, 0));
        Double silverRate=Double.longBitsToDouble(pref.getLong(PREF_SILVER_RATE, 0));
        setPricesTextView(goldRate,silverRate);

        if(goldRate==0||silverRate==0)
        {
            rateFragment.show(fm, "fragment_rates");
        }
        editRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                rateFragment.show(fm, "fragment_rates");
            }
        });
    }

    private void changeRates(double goldRate, double silverRate)
    {
        setRatePref(goldRate, silverRate);
        setPricesTextView(goldRate, silverRate);
    }
    private void setRatePref(double goldRate, double silverRate)
    {
        setGoldRatePref(goldRate);
        setSilverRatePref(silverRate);
    }
    private void setGoldRatePref(double goldRate)
    {
        SharedPreferences.Editor edit = pref.edit();
        edit.putLong(PREF_GOLD_RATE, Double.doubleToRawLongBits(goldRate));
        edit.commit();
    }

    private void setSilverRatePref(double silverRate)
    {
        SharedPreferences.Editor edit = pref.edit();
        edit.putLong(PREF_SILVER_RATE, Double.doubleToRawLongBits(silverRate));
        edit.commit();
    }

    private void setPricesTextView(double goldRate,double silverRate)
    {
        setGoldTextView(goldRate);
        setSilverTextView(silverRate);
    }

    @Override
    public void onRateRecieved(String goldRate, String silverRate)
    {
        changeRates(Double.parseDouble(goldRate), Double.parseDouble(silverRate));
        loadQualifiedGirviTransactions();
        galanaAdapter.notifyDataSetChanged();
    }

    private void setGoldTextView(double goldRate)
    {
        goldPriceTextView.setText("Au@ " + goldRate);
    }
    private void setSilverTextView(double silverRate){ silverPriceTextView.setText("Ag@ "+silverRate);}
    private void setUpDrawer() {
        String[] list = {"Girvi", "Khata Udhar","Backup","Restore"};
        drawer.setAdapter(new ArrayAdapter<String>(this, R.layout.navigation_drawer_list_item, list));
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_menu_white_24dp, R.string.drawer_open, R.string.drawer_closed);
        drawerLayout.setDrawerListener(toggle);
        drawer.setOnItemClickListener(new DrawerItemClickListener(this,drawerLayout));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
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

    private void changeAdapterWithNewListInstance(ArrayList<GirviTransaction> girviTransactionsList)
    {
        galanaAdapter=new GalanaAdapter(this,girviTransactionsList,girviTransactionEvaluator);
        galanaRecyclerView.swapAdapter(galanaAdapter,true);
    }
}
