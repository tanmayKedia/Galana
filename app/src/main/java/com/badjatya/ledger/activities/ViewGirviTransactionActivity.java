package com.badjatya.ledger.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.badjatya.ledger.Database.CursorWrappers.GirviTransactionCursorWrapper;
import com.badjatya.ledger.Database.CursorWrappers.TransactionCursorWrapper;
import com.badjatya.ledger.Database.DatabaseHelper;
import com.badjatya.ledger.Database.DatabaseSchema;
import com.badjatya.ledger.Database.InsertAndQueryDb;
import com.badjatya.ledger.Database.ModelToContentValue;
import com.badjatya.ledger.Fragments.AlertFragment;
import com.badjatya.ledger.R;
import com.badjatya.ledger.Utils.GirviTransactionEvaluator;
import com.badjatya.ledger.Utils.TimeConverter;
import com.badjatya.ledger.adapters.TransactionAdapter;
import com.badjatya.ledger.models.GirviTransaction;
import com.badjatya.ledger.models.InterestRates;
import com.badjatya.ledger.models.MetalType;
import com.badjatya.ledger.models.Transaction;
import com.badjatya.ledger.Database.DatabaseSchema.TransactionTable;

import org.joda.time.LocalDate;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tanmay on 9/30/2016.
 */
public class ViewGirviTransactionActivity extends AppCompatActivity implements AlertFragment.OnOkayPressedGirviInterface {
    @BindView(R.id.view_girvi_scrollView) ScrollView scrollView;
    @BindView(R.id.view_girvi_transaction_scrollView) ScrollView transactionScrollView;
    @BindView(R.id.view_girvi_recycler) RecyclerView recyclerView;
    @BindView(R.id.view_girvi_add_galana_button) Button galanaButton;
    @BindView(R.id.view_girvi_add_entry_button) Button addTransactionButton;
    @BindView(R.id.view_girvi_name_ans)TextView nameAns;
    @BindView(R.id.view_girvi_fathers_name_ans)TextView fatherNameAns;
    @BindView(R.id.view_girvi_village_name_ans)TextView villageAns;
    @BindView(R.id.view_girvi_caste_ans)TextView casteAns;
    @BindView(R.id.view_girvi_phone_ans)TextView phoneAns;
    @BindView(R.id.view_girvi_type_ans)TextView metalTypeAns;
    @BindView(R.id.view_girvi_item_name_ans)TextView itemNameAns;
    @BindView(R.id.view_girvi_item_weight_ans)TextView weightAns;
    @BindView(R.id.view_girvi_tunch_ans)TextView tunchAns;
    @BindView(R.id.view_girvi_item_interest_ans)TextView interestRateAns;
    @BindView(R.id.view_girvi_metal_rate_ans)TextView metalRateAns;
    @BindView(R.id.view_girvi_value_calc_ans)TextView valuCalcAns;
    @BindView(R.id.view_girvi_item_money_loaned_ans)TextView amountLoaned;
    @BindView(R.id.view_detail_frame)FrameLayout detailframeLayout;
    @BindView(R.id.view_melted_textView)TextView meltedTextView;
    @BindView(R.id.view_girvi_add_settle_button) Button settleButton;
    @BindView(R.id.view_girvi_date_ans) TextView dateAns;

    private static final String EXTRA_GIRVI_TRANSACTION_ID="girvi_transaction_id_intent_extra";
    private static final String EXTRA_TRANSACTION_ID="transaction_id_intent_extra";
    private static final String EXTRA_CHECKBOX_QUALIFIER="transaction_checkbox_qualifier_extra";
    private static final int REQUEST_CODE_ADD_TRANSACTION=001;
    private static final int BUTTON_CODE_MELTED_OKAY_BUTTON=002;
    private static final int BUTTON_CODE_SETTLE_OKAY_BUTTON=003;
    private GirviTransaction girviTransaction;
    private ArrayList<Transaction> transactionArrayList=new ArrayList<Transaction>();
    SQLiteDatabase sqLiteDatabase;
    InsertAndQueryDb insertAndQueryDb;
    private FragmentManager fm;
    private TransactionAdapter transactionAdapter;
    GirviTransactionEvaluator gte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_girvi_transaction);
        ButterKnife.bind(this);
        sqLiteDatabase=new DatabaseHelper(this).getWritableDatabase();
        insertAndQueryDb =new InsertAndQueryDb(sqLiteDatabase);
        Display display = ((WindowManager)
                getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int height =display.getHeight();
        detailframeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 3));
        //scrollView.setLayoutParams();
        scrollView.setScrollbarFadingEnabled(false);
        transactionScrollView.setScrollbarFadingEnabled(false);
        loadGirviTransaction(getIntent().getStringExtra(EXTRA_GIRVI_TRANSACTION_ID));
        loadTransactionList();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fm=getSupportFragmentManager();
        transactionAdapter=new TransactionAdapter(this,fm,transactionArrayList);
        setUpMeltedIndicators(girviTransaction.getSettled());
        recyclerView.setAdapter(transactionAdapter);

        galanaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertFragment.newInstance("Are you sure you want to mark this as melted?", BUTTON_CODE_MELTED_OKAY_BUTTON)
                        .show(fm, "m1");
            }
        });

        settleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gte=new GirviTransactionEvaluator(getBaseContext());
                AlertFragment.newInstance("Settle amount is Rs. "+ new DecimalFormat("#.##").format(gte.settleAmount(girviTransaction))+". Are you sure you want to settle?",
                        BUTTON_CODE_SETTLE_OKAY_BUTTON
                        ).show(fm,"m3");
            }
        });

        addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(),AddTransactionActivity.class);
                intent.putExtra(EXTRA_TRANSACTION_ID,girviTransaction.getId());
                intent.putExtra(EXTRA_CHECKBOX_QUALIFIER,0);
                startActivityForResult(intent, REQUEST_CODE_ADD_TRANSACTION);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_view_transaction_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.activity_girvi_delete_button)
        {
            deleteGirviTransaction(girviTransaction.getId());
            setResult(RESULT_OK);
            Toast.makeText(this,"Transaction Deleted",Toast.LENGTH_LONG).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteGirviTransaction(String girviTransactionId)
    {
        insertAndQueryDb.deleteGirviTransaction(DatabaseSchema.GirviTransactionTable.Cols.UUID+"=?",new String[]{girviTransactionId});
        deleteRelatedTransactionEntries(girviTransactionId);
    }
    private void deleteRelatedTransactionEntries(String girviTransactionId)
    {
        insertAndQueryDb.deleteTransactions(TransactionTable.Cols.GIRVI_TRANSACTION_ID+"=?",new String[]{girviTransactionId});
    }
    {

    }
    @Override
    public void deleteOkayPressed()
    {
        insertAndQueryDb.deleteTransactions(TransactionTable.Cols.ID+"=?",new String []{transactionAdapter.deleteTransactionId});
        showLongToast("Transaction deleted!");
        transactionArrayList.clear();
        loadTransactionList();
        transactionAdapter.notifyDataSetChanged();

    }

    @Override
    public void meltOkayPressed()
    {
        if(girviTransaction.getSettled()==3)
        {
            showLongToast("Already Marked As Melted!");
            return;
        }
        girviTransaction.setSettled(003);
        setUpMeltedIndicators(girviTransaction.getSettled());
        ContentValues contentValues=new ModelToContentValue().girviTransactionToContentValue(girviTransaction);
        insertAndQueryDb.updateGirviTransaction(contentValues, DatabaseSchema.GirviTransactionTable.Cols.UUID + "=?", new String[]{girviTransaction.getId()});
    }

    @Override
    public void settleOkayPressed() {

        if(girviTransaction.getSettled()==3)
        {
            showLongToast("Already Marked As Melted!");
            return;
        }
        girviTransaction.setSettled(002);
        setUpMeltedIndicators(girviTransaction.getSettled());
        ContentValues contentValues=new ModelToContentValue().girviTransactionToContentValue(girviTransaction);
        insertAndQueryDb.updateGirviTransaction(contentValues, DatabaseSchema.GirviTransactionTable.Cols.UUID + "=?", new String[]{girviTransaction.getId()});

        Transaction transaction=new Transaction();
        transaction.setAmount(Double.parseDouble( new DecimalFormat("#.##").format(gte.settleAmount(girviTransaction))));
        LocalDate localDate=LocalDate.now();
        Log.d("voldemort", localDate.toString());
        transaction.setDate(TimeConverter.convertDateStringToEpoch(localDate.dayOfMonth().get() + "/" + localDate.monthOfYear().get() + "/" + localDate.year().get()));
        transaction.setGirviTransactionId(girviTransaction.getId());
        ContentValues transactionContentValue=new ModelToContentValue().transactionToContentValue(transaction);
        insertAndQueryDb.insertTransaction(transactionContentValue);
        transactionArrayList.add(transaction);
        transactionAdapter.notifyDataSetChanged();
    }

    private void showLongToast(String s)
    {
        Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
    }


    private void loadGirviTransaction(String uuid)
    {
        GirviTransactionCursorWrapper gcw= insertAndQueryDb.queryGirviTransactionTable(DatabaseSchema.GirviTransactionTable.Cols.UUID+"=?",new String[]{uuid});
        try
        {
            gcw.moveToFirst();
            girviTransaction=gcw.getGirviTransaction();
        }
        finally {
            gcw.close();
        }
        dateAns.setText(TimeConverter.convertEpochToDateString(girviTransaction.getDate()));
        nameAns.setText(girviTransaction.getName());
        fatherNameAns.setText(girviTransaction.getFatherName());
        villageAns.setText(girviTransaction.getVillage());
        casteAns.setText(girviTransaction.getCaste());
        phoneAns.setText(girviTransaction.getPhoneNumber());
        metalTypeAns.setText(MetalType.getTypeFromIndex(girviTransaction.getMetalType()));
        itemNameAns.setText(girviTransaction.getItemName());
        weightAns.setText(new Double(girviTransaction.getWeight()).toString());
        tunchAns.setText(new Double(girviTransaction.getTunch()) + "%");
        interestRateAns.setText(InterestRates.getRateFormIndex(girviTransaction.getInterestPercentage()));
        metalRateAns.setText(new Double(girviTransaction.getMetalPrice()).toString());
        valuCalcAns.setText(new Double(girviTransaction.getCalcValue()).toString());
        amountLoaned.setText(new Double(girviTransaction.getMoneyGiven()).toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK)
        {
            return;
        }

        if(requestCode== REQUEST_CODE_ADD_TRANSACTION) {
            transactionArrayList.clear();
            loadTransactionList();
            transactionAdapter.notifyDataSetChanged();
        }
    }

    private void loadTransactionList()
    {
        TransactionCursorWrapper tcw= insertAndQueryDb.queryTransaction(TransactionTable.Cols.GIRVI_TRANSACTION_ID+"=?",new String[]{girviTransaction.getId()});
        try
        {
            tcw.moveToFirst();

            while(!tcw.isAfterLast())
            {
                transactionArrayList.add(tcw.getTransaction());
                tcw.moveToNext();
            }
        }
        finally
        {
            tcw.close();
        }
    }
    private void setUpMeltedIndicators(int settled)
    {
        if(settled==3)
        {
            meltedTextView.setVisibility(View.VISIBLE);
            meltedTextView.setText("MELTED");
            detailframeLayout.setBackgroundColor(getResources().getColor(R.color.transaparentRed));
            transactionAdapter.settled=3;
            addTransactionButton.setEnabled(false);
            galanaButton.setEnabled(false);
            settleButton.setEnabled(false);
        }
        else if(settled==2)
        {
            meltedTextView.setVisibility(View.VISIBLE);
            meltedTextView.setText("SETTLED");
            detailframeLayout.setBackgroundColor(getResources().getColor(R.color.transaparentRed));
            transactionAdapter.settled=2;
            addTransactionButton.setEnabled(false);
            galanaButton.setEnabled(false);
            settleButton.setEnabled(false);
        }
        else
        {
            meltedTextView.setVisibility(View.GONE);
            detailframeLayout.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        }
    }


}
