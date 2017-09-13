package com.badjatya.ledger.activities;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.badjatya.ledger.Database.DatabaseHelper;
import com.badjatya.ledger.Database.InsertAndQueryDb;
import com.badjatya.ledger.Database.ModelToContentValue;
import com.badjatya.ledger.Utils.TimeConverter;
import com.badjatya.ledger.models.Transaction;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;


import com.badjatya.ledger.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tanmay on 10/3/2016.
 */
public class AddTransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{
    @BindView(R.id.add_new_transaction_date_ans) TextView dateTextView;
    @BindView(R.id.add_new_transaction_amount_ans)EditText amountEditText;
    @BindView(R.id.add_new_transaction_add_button) Button createButton;
    @BindView(R.id.add_new_transaction_naya_udhar) CheckBox nayaUdharCheckBox;
    private String girviUuid;
    private int checkBoxQualifier;
    private DatePickerDialog dpd;
    private FragmentManager fm;
    private static final String EXTRA_TRANSACTION_ID="transaction_id_intent_extra";
    private static final String EXTRA_CHECKBOX_QUALIFIER="transaction_checkbox_qualifier_extra";

    SQLiteDatabase db;
    InsertAndQueryDb insertAndQueryDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        ButterKnife.bind(this);

        fm=getFragmentManager();
        db=new DatabaseHelper(this).getWritableDatabase();
        insertAndQueryDb =new InsertAndQueryDb(db);
        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        girviUuid=getIntent().getStringExtra(EXTRA_TRANSACTION_ID);
        checkBoxQualifier=getIntent().getIntExtra(EXTRA_CHECKBOX_QUALIFIER,0);

        if(checkBoxQualifier==1)
        {
            nayaUdharCheckBox.setVisibility(View.VISIBLE);
        }

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show(fm, "Datepickerdialog");
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTransaction();
            }
        });
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(monthOfYear + 1)+"/"+year;
        dateTextView.setText(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Linking the callback from the retained DatepickerDialog DialogFragment to this activity
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    private void createTransaction()
    {
        Transaction transaction=new Transaction();
        transaction.setGirviTransactionId(girviUuid);
        if(dateTextView.getText().length()==0)
        {
            showLongToast("Enter Date First!");
            return;
        }
        transaction.setDate(TimeConverter.convertDateStringToEpoch(dateTextView.getText().toString()));

        if(amountEditText.getText().length()==0)
        {
            showLongToast("Enter Amount!");
            return;
        }
        if(checkBoxQualifier==1&&nayaUdharCheckBox.isChecked()==true)
        {
            transaction.setAmount(-1*Double.parseDouble(amountEditText.getText().toString()));
        }
        else
        {
            transaction.setAmount(Double.parseDouble(amountEditText.getText().toString()));
        }
        ContentValues contentValues=new ModelToContentValue().transactionToContentValue(transaction);
        setResult(RESULT_OK);
        insertAndQueryDb.insertTransaction(contentValues);
        showLongToast("Transaction Added");
        finish();
    }
    private void showLongToast(String s)
    {
        Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
    }


}


