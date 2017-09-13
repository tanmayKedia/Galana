package com.badjatya.ledger.Rakam.AddRakam;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.badjatya.ledger.Database.DatabaseHelper;
import com.badjatya.ledger.Database.InsertAndQueryDb;
import com.badjatya.ledger.Database.ModelToContentValue;
import com.badjatya.ledger.R;
import com.badjatya.ledger.Rakam.RakamTransaction;
import com.badjatya.ledger.Utils.TimeConverter;
import com.badjatya.ledger.activities.SearchAndAdd;
import com.badjatya.ledger.models.InterestRates;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tanmay on 11/28/2016.
 */
public class AddRakamActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.add_rakam_transaction_date_ans)TextView transactionDate;
    @BindView(R.id.add_rakam_transaction_name_ans)EditText name;
    @BindView(R.id.add_rakam_transaction_fathers_name_ans)EditText fathersName;
    @BindView(R.id.add_rakam_transaction_village_ans)TextView villageName;
    @BindView(R.id.add_rakam_transaction_caste_ans)TextView caste;
    @BindView(R.id.add_rakam_transaction_mobileno_ans)EditText mobileNumber;
    @BindView(R.id.add_rakam_transaction_item_name_ans)EditText itemName;
    @BindView(R.id.add_rakam_transaction_interest_ans)TextView interestPercentage;
    @BindView(R.id.add_rakam_transaction_value_ans)TextView itemValue;
    @BindView(R.id.add_rakam_transaction_money_loaned_ans)EditText moneyLoaned;
    @BindView(R.id.add_number_from_contact)ImageButton addNumberFromContacts;
    @BindView(R.id.add_rakam_transaction_create_button) Button createTransactionButton;

    private static final int VILLAGES=001;
    private static final int CASTE=002;
    private static final int INTEREST_RATES=004;
    private static final int REQUEST_CODE_CONTACT_NUMBER=005;
    private boolean flag=true;
    private FragmentManager fm;
    private DatePickerDialog dpd;
    SQLiteDatabase db;
    InsertAndQueryDb insertAndQueryDb;
    ModelToContentValue modelToContentValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rakam_transaction);

        ButterKnife.bind(this);
        fm=getFragmentManager();

        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );


        setupListeners();
        db= new DatabaseHelper(this).getWritableDatabase();
        insertAndQueryDb =new InsertAndQueryDb(db);
        modelToContentValue= new ModelToContentValue();
    }

    private void setupListeners() {

        createTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTransaction();
            }
        });

        transactionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show(fm, "Datepickerdialog");
            }
        });

        name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                name.setCursorVisible(true);
                return false;
            }
        });
        fathersName.setCursorVisible(true);

        villageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchAndAdd.class);
                intent.putExtra("LOGIC", VILLAGES);
                startActivityForResult(intent, VILLAGES);
            }
        });

        caste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchAndAdd.class);
                intent.putExtra("LOGIC", CASTE);
                startActivityForResult(intent, CASTE);
            }
        });

        addNumberFromContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getContactNumberIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(getContactNumberIntent, REQUEST_CODE_CONTACT_NUMBER);
            }
        });

        interestPercentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchAndAdd.class);
                intent.putExtra("LOGIC", INTEREST_RATES);
                startActivityForResult(intent, INTEREST_RATES);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK)
        {
            return;
        }

        if(requestCode==REQUEST_CODE_CONTACT_NUMBER && data!=null)
        {
            Uri contactUri=data.getData();
            String[] queryFields=new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor c=getContentResolver().query(contactUri,queryFields,null,null,null);
            try
            {
                if(c.getCount()==0)
                {
                    return;
                }
                c.moveToFirst();
                String contactNumber=c.getString(0);
                mobileNumber.setText(contactNumber);
                mobileNumber.setSelection(mobileNumber.getText().length());
            }
            finally {
                c.close();
            }
        }

        if(requestCode==VILLAGES&&data!=null)
        {
            villageName.setText(data.getStringExtra("NAME"));
        }
        if(requestCode==CASTE&&data!=null)
        {
            caste.setText(data.getStringExtra("NAME"));
        }

        if(requestCode==INTEREST_RATES&&data!=null)
        {
            interestPercentage.setText(data.getStringExtra("NAME"));
        }
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) 
    {
        String date = dayOfMonth+"/"+(monthOfYear + 1)+"/"+year;
        transactionDate.setText(date);
    }
    
    private void createTransaction()
    {
        RakamTransaction rakamTransaction=new RakamTransaction();

        if(transactionDate.getText().length()==0)
        {
            showLongToast("Enter the date of the transaction!");
            return;
        }
        rakamTransaction.setDate(TimeConverter.convertDateStringToEpoch(transactionDate.getText().toString()));

        if(name.getText().length()==0)
        {
            showLongToast("Enter name!");
            return;
        }
        rakamTransaction.setName(name.getText().toString());

        if(fathersName.getText().length()==0)
        {
            showLongToast("Enter Father's name!");
            return;
        }
        rakamTransaction.setFatherName(fathersName.getText().toString());

        if(villageName.getText().length()==0)
        {
            showLongToast("Enter Village name!");
            return;
        }
        rakamTransaction.setVillage(villageName.getText().toString());

        if(caste.getText().length()==0)
        {
            showLongToast("Enter Caste!");
            return;
        }
        rakamTransaction.setCaste(caste.getText().toString());

        if(mobileNumber.getText().length()==0)
        {
            showLongToast("Enter Mobile Number!");
            return;
        }
        rakamTransaction.setPhoneNumber(mobileNumber.getText().toString());

        if(itemName.getText().length()==0)
        {
            showLongToast("Enter Item Name!");
            return;
        }
        rakamTransaction.setItemName(itemName.getText().toString());

        if(interestPercentage.getText().length()==0)
        {
            showLongToast("Enter Interest!");
            return;
        }
        rakamTransaction.setInterestPercentage(InterestRates.getIndexFromRate(interestPercentage.getText().toString()));
        
        //enter value calc
        if(itemValue.getText().length()==0)
        {
            showLongToast("Enter Item Value");
            return;
        }
        rakamTransaction.setItemValue(Double.parseDouble(itemValue.getText().toString()));

        if(moneyLoaned.getText().length()==0)
        {
            showLongToast("Enter the Loan Amount");
            return;
        }
        else if(Double.parseDouble(moneyLoaned.getText().toString())>Double.parseDouble(itemValue.getText().toString()))
        {
            showLongToast("Loan can Not be bigger than Value!");
            return;
        }

        rakamTransaction.setMoneyGiven(Double.parseDouble(moneyLoaned.getText().toString()));
        rakamTransaction.setSettled(1);

        ContentValues contentValues=modelToContentValue.rakamTransactionToContentValues(rakamTransaction);
        insertAndQueryDb.insertRakamTransaction(contentValues);
        setResult(RESULT_OK);
        showLongToast("Transaction Added!");
        finish();
    }

    private void showLongToast(String s)
    {
        Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
    }
}
