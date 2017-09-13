package com.badjatya.ledger.Rakam.ViewRakam;

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

import com.badjatya.ledger.Database.CursorWrappers.RakamTransactionCursorWrapper;
import com.badjatya.ledger.Database.CursorWrappers.TransactionCursorWrapper;
import com.badjatya.ledger.Database.DatabaseHelper;
import com.badjatya.ledger.Database.DatabaseSchema;
import com.badjatya.ledger.Database.InsertAndQueryDb;
import com.badjatya.ledger.Database.ModelToContentValue;
import com.badjatya.ledger.Fragments.AlertFragment;
import com.badjatya.ledger.R;
import com.badjatya.ledger.Rakam.RakamTransaction;
import com.badjatya.ledger.Rakam.RakamTransactionEvaluator;
import com.badjatya.ledger.Utils.GirviTransactionEvaluator;
import com.badjatya.ledger.Utils.TimeConverter;
import com.badjatya.ledger.activities.AddTransactionActivity;
import com.badjatya.ledger.adapters.TransactionAdapter;
import com.badjatya.ledger.models.InterestRates;
import com.badjatya.ledger.models.Transaction;

import org.joda.time.LocalDate;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tanmay on 11/29/2016.
 */
public class ViewRakamTransaction extends AppCompatActivity implements AlertFragment.OkayPressedRakamInterface {
    @BindView(R.id.view_rakam_scrollView)
    ScrollView scrollView;
    @BindView(R.id.view_rakam_transaction_scrollView) ScrollView transactionScrollView;
    @BindView(R.id.view_rakam_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.view_rakam_add_entry_button) Button addTransactionButton;
    @BindView(R.id.view_rakam_name_ans)TextView nameAns;
    @BindView(R.id.view_rakam_fathers_name_ans)TextView fatherNameAns;
    @BindView(R.id.view_rakam_village_name_ans)TextView villageAns;
    @BindView(R.id.view_rakam_caste_ans)TextView casteAns;
    @BindView(R.id.view_rakam_phone_ans)TextView phoneAns;
    @BindView(R.id.view_rakam_item_name_ans)TextView itemNameAns;
    @BindView(R.id.view_rakam_item_interest_ans)TextView interestRateAns;
    @BindView(R.id.view_rakam_value_calc_ans)TextView itemValue;
    @BindView(R.id.view_rakam_item_money_loaned_ans)TextView amountLoaned;
    @BindView(R.id.view_detail_frame)FrameLayout detailframeLayout;
    @BindView(R.id.view_melted_textView)TextView meltedTextView;
    @BindView(R.id.view_rakam_add_settle_button) Button settleButton;
    @BindView(R.id.view_rakam_date_ans) TextView dateAns;

    private static final String EXTRA_CHECKBOX_QUALIFIER="transaction_checkbox_qualifier_extra";
    private static final String EXTRA_TRANSACTION_ID ="transaction_id_intent_extra";
    private static final String EXTRA_RAKAM_TRANSACTION_ID="rakam_transaction_id_intent_extra";
    private static final int REQUEST_CODE_ADD_TRANSACTION=001;
    private static final int BUTTON_CODE_MELTED_OKAY_BUTTON=002;
    private static final int BUTTON_CODE_SETTLE_OKAY_BUTTON=003;
    private RakamTransaction rakamTransaction;
    private ArrayList<Transaction> transactionArrayList=new ArrayList<Transaction>();
    SQLiteDatabase sqLiteDatabase;
    InsertAndQueryDb insertAndQueryDb;
    private FragmentManager fm;
    private TransactionAdapter transactionAdapter;
    RakamTransactionEvaluator rte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_rakam_transaction);
        ButterKnife.bind(this);
        Display display = ((WindowManager)
                getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int height =display.getHeight();
        detailframeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 3));
        //scrollView.setLayoutParams();
        scrollView.setScrollbarFadingEnabled(false);
        transactionScrollView.setScrollbarFadingEnabled(false);
        rte=new RakamTransactionEvaluator(getBaseContext());
        sqLiteDatabase=new DatabaseHelper(this).getWritableDatabase();
        insertAndQueryDb =new InsertAndQueryDb(sqLiteDatabase);
        loadRakamTransaction(getIntent().getStringExtra(EXTRA_RAKAM_TRANSACTION_ID));
        loadTransactionList();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fm=getSupportFragmentManager();
        transactionAdapter=new TransactionAdapter(this,fm,transactionArrayList);
        recyclerView.setAdapter(transactionAdapter);
        setUpSettledIndicators(rakamTransaction.getSettled());

        addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AddTransactionActivity.class);
                intent.putExtra(EXTRA_TRANSACTION_ID, rakamTransaction.getId());
                //to make the checkbox visible
                intent.putExtra(EXTRA_CHECKBOX_QUALIFIER,1);
                startActivityForResult(intent, REQUEST_CODE_ADD_TRANSACTION);
            }
        });

        settleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertFragment.newInstance("Settle amount is Rs. "+ new DecimalFormat("#.##").format(rte.settleAmount(rakamTransaction))+". Are you sure you want to settle?",
                        BUTTON_CODE_SETTLE_OKAY_BUTTON
                ).show(fm,"m3");
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
           deleteRakamTransaction(rakamTransaction.getId());
            setResult(RESULT_OK);
            Toast.makeText(this,"Transaction Deleted",Toast.LENGTH_LONG).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteRakamTransaction(String rakamTransactionId)
    {
        insertAndQueryDb.deleteRakamTransaction(DatabaseSchema.RakamTransactionTable.Cols.UUID+"=?",new String[]{rakamTransactionId});
        deleteRelatedTransactions(rakamTransactionId);
    }
    private void deleteRelatedTransactions(String rakamTransactionId)
    {
        insertAndQueryDb.deleteTransactions(DatabaseSchema.TransactionTable.Cols.GIRVI_TRANSACTION_ID+"=?",new String[]{rakamTransactionId});
    }
    private void loadTransactionList()
    {
        TransactionCursorWrapper tcw= insertAndQueryDb.queryTransaction(DatabaseSchema.TransactionTable.Cols.GIRVI_TRANSACTION_ID+"=?",new String[]{rakamTransaction.getId()});
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
    private void loadRakamTransaction(String uuid)
    {
        RakamTransactionCursorWrapper gcw= insertAndQueryDb.queryRakamTransactionTable(DatabaseSchema.RakamTransactionTable.Cols.UUID + "=?", new String[]{uuid});

        try
        {
            gcw.moveToFirst();
            rakamTransaction=gcw.getRakamTransaction();
        }
        finally {
            gcw.close();
        }
        dateAns.setText(TimeConverter.convertEpochToDateString(rakamTransaction.getDate()));
        nameAns.setText(rakamTransaction.getName());
        fatherNameAns.setText(rakamTransaction.getFatherName());
        villageAns.setText(rakamTransaction.getVillage());
        casteAns.setText(rakamTransaction.getCaste());
        phoneAns.setText(rakamTransaction.getPhoneNumber());
        itemNameAns.setText(rakamTransaction.getItemName());
        interestRateAns.setText(InterestRates.getRateFormIndex(rakamTransaction.getInterestPercentage()));
        itemValue.setText(new Double(rakamTransaction.getItemValue()).toString());
        amountLoaned.setText(new Double(rakamTransaction.getMoneyGiven()).toString());
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

    private void setUpSettledIndicators(int settled)
    {
       if(settled==2)
        {
            meltedTextView.setVisibility(View.VISIBLE);
            meltedTextView.setText("SETTLED");
            detailframeLayout.setBackgroundColor(getResources().getColor(R.color.transaparentRed));
            transactionAdapter.settled=2;
            addTransactionButton.setEnabled(false);

            settleButton.setEnabled(false);
        }
        else
        {
            meltedTextView.setVisibility(View.GONE);
            detailframeLayout.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        }
    }

    @Override
    public void deleteOkayPressed() {
        insertAndQueryDb.deleteTransactions(DatabaseSchema.TransactionTable.Cols.ID+"=?",new String []{transactionAdapter.deleteTransactionId});
        showLongToast("Transaction deleted!");
        transactionArrayList.clear();
        loadTransactionList();
        transactionAdapter.notifyDataSetChanged();
    }

    @Override
    public void settleOkayPressed()
    {
        rakamTransaction.setSettled(002);
        setUpSettledIndicators(rakamTransaction.getSettled());
        ContentValues contentValues=new ModelToContentValue().rakamTransactionToContentValues(rakamTransaction);
        insertAndQueryDb.updateRakamTransaction(contentValues, DatabaseSchema.RakamTransactionTable.Cols.UUID + "=?", new String[]{rakamTransaction.getId()});

        Transaction transaction=new Transaction();
        transaction.setAmount(Double.parseDouble( new DecimalFormat("#.##").format(rte.settleAmount(rakamTransaction))));
        LocalDate localDate=LocalDate.now();
        Log.d("voldemort",localDate.toString());
        transaction.setDate(TimeConverter.convertDateStringToEpoch(localDate.dayOfMonth().get() + "/" + localDate.monthOfYear().get() + "/" + localDate.year().get()));
        transaction.setGirviTransactionId(rakamTransaction.getId());
        ContentValues transactionContentValue=new ModelToContentValue().transactionToContentValue(transaction);
        insertAndQueryDb.insertTransaction(transactionContentValue);
        transactionArrayList.add(transaction);
        transactionAdapter.notifyDataSetChanged();
    }


    private void showLongToast(String s)
    {
        Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
    }
}
