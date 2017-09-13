package com.badjatya.ledger.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.badjatya.ledger.Utils.TimeConverter;
import com.badjatya.ledger.models.GirviTransaction;
import com.badjatya.ledger.models.InterestRates;
import com.badjatya.ledger.models.MetalType;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tanmay on 9/30/2016.
 */
public class AddGirviTransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.add_girvi_transaction_date_ans)TextView transactionDate;
    @BindView(R.id.add_girvi_transaction_name_ans)EditText name;
    @BindView(R.id.add_girvi_transaction_fathers_name_ans)EditText fathersName;
    @BindView(R.id.add_girvi_transaction_village_ans)TextView villageName;
    @BindView(R.id.add_girvi_transaction_caste_ans)TextView caste;
    @BindView(R.id.add_girvi_transaction_mobileno_ans)EditText mobileNumber;
    @BindView(R.id.add_girvi_transaction_type_ans)TextView metalType;
    @BindView(R.id.add_girvi_transaction_item_name_ans)EditText itemName;
    @BindView(R.id.add_girvi_transaction_item_weight_ans)EditText itemWeight;
    @BindView(R.id.add_girvi_transaction_item_tunch_ans) EditText itemTunch;
    @BindView(R.id.add_girvi_transaction_interest_ans)TextView interestPercentage;
    @BindView(R.id.add_girvi_transaction_gold_rate_ans)EditText metaldRateEditText;
    @BindView(R.id.add_girvi_transaction_value_ans)TextView valueCalculated;
    @BindView(R.id.add_girvi_transaction_money_loaned_ans)EditText moneyLoaned;
    @BindView(R.id.add_number_from_contact)ImageButton addNumberFromContacts;
    @BindView(R.id.add_girvi_transaction_create_button) Button createTransactionButton;

    private DatePickerDialog dpd;
    private static final int REQUEST_CODE_CONTACT_NUMBER=005;
    private static final int VILLAGES=001;
    private static final int CASTE=002;
    private static final int METALTYPE=003;
    private static final int INTEREST_RATES=004;
    private static final String PREF_GOLD_RATE="gold_rate";
    private static final String PREF_SILVER_RATE="silver_rate";
    private boolean flag=true;
    private FragmentManager fm;
    SQLiteDatabase db;
    InsertAndQueryDb insertAndQueryDb;
    ModelToContentValue modelToContentValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_girvi_transaction);
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
        moneyLoaned.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(flag==false)
                {
                    flag=true;
                    return;
                }

                if(valueCalculated.getText().length()>0)
                {
                    if(s.toString().length()>0)
                    {
                        double loanAmount = Double.parseDouble(s.toString());
                        double calcValue = Double.parseDouble(valueCalculated.getText().toString());
                        if (loanAmount - calcValue > 0)
                        {
                            showLongToast("You cannot loan more than the value of the item!");
                            moneyLoaned.setText("");
                            flag = false;
                        }
                    }
                }
                else
                {
                    flag=false;
                    showLongToast("Value has not been Calculated yet");
                    moneyLoaned.setText("");
                }
            }
        });

        metaldRateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(flag==false)
                {
                    flag=true;
                    return;
                }
                validateAndSetValue();
            }
        });

        itemWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateAndSetValue();
            }
        });


        itemTunch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(flag==false)
                {
                    flag=true;
                    return;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if(s.length()>0)
                    {
                        double value= Double.parseDouble(s.toString());
                        if(value<0||value>100)
                        {
                            Toast.makeText(getBaseContext(),"Tunch should be b/w 0 and 100",Toast.LENGTH_LONG).show();
                            flag=false;
                            itemTunch.setText("");
                        }
                        else
                        {
                            validateAndSetValue();
                        }
                    }
                    //In case we clear the value from tunch field, then validation needs to take place(It will fail) and so the
                    //calculatedValue will be set to ""(check out the implementation of validateAndSetValue)
                    else if(s.length()==0)
                    {
                        validateAndSetValue();
                    }
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

        metalType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchAndAdd.class);
                intent.putExtra("LOGIC",METALTYPE);
                startActivityForResult(intent,METALTYPE);
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(monthOfYear + 1)+"/"+year;
        transactionDate.setText(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Linking the callback from the retained DatepickerDialog DialogFragment to this activity
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
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
        if(requestCode==METALTYPE&&data!=null)
        {
            metalType.setText(data.getStringExtra("NAME"));
            flag=false;
            setMetalRate(data.getStringExtra("NAME"));
            validateAndSetValue();
        }
        if(requestCode==INTEREST_RATES&&data!=null)
        {
            interestPercentage.setText(data.getStringExtra("NAME"));
        }
    }

    private void setMetalRate(String metalName)
    {   SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Double goldRate = Double.longBitsToDouble(pref.getLong(PREF_GOLD_RATE, 0));
        Double silverRate=Double.longBitsToDouble(pref.getLong(PREF_SILVER_RATE,0));
        if(metalName.equals(MetalType.getMetalArray()[0]))
        {
            metaldRateEditText.setText(goldRate+"");
            metaldRateEditText.setSelection(metaldRateEditText.getText().length());
        }
        else if(metalName.equals(MetalType.getMetalArray()[1]))
        {
            metaldRateEditText.setText(silverRate+"");
            metaldRateEditText.setSelection(metaldRateEditText.getText().length());
        }
    }

    private void validateAndSetValue()
    {
        if(itemTunch.getText().length()>0
                && itemWeight.getText().length()>0
                && metaldRateEditText.getText().length()>0
                )
        {
            double tunch=Double.parseDouble(itemTunch.getText().toString());
            double weight=Double.parseDouble(itemWeight.getText().toString());
            double price=0;
            if(metalType.getText().equals("Gold"))
            {
                price=Double.parseDouble(metaldRateEditText.getText().toString())/10;
            }
            else if(metalType.getText().equals("Silver"))
            {
                price=Double.parseDouble(metaldRateEditText.getText().toString())/1000;
            }

            double calculatedValue=((tunch/100)*weight)*price;
            valueCalculated.setText(calculatedValue+"");
            Toast.makeText(getBaseContext(), "Calculated Value Changed", Toast.LENGTH_LONG);
        }
        else
        {
            valueCalculated.setText("");
        }
    }

    private void showLongToast(String s)
    {
        Toast.makeText(getBaseContext(),s,Toast.LENGTH_LONG).show();
    }

    private void createTransaction()
    {
        GirviTransaction girviTransaction=new GirviTransaction(UUID.randomUUID().toString());
        if(transactionDate.getText().length()==0)
        {
            showLongToast("Enter the date of the transaction!");
            return;
        }
        girviTransaction.setDate(TimeConverter.convertDateStringToEpoch(transactionDate.getText().toString()));

        if(name.getText().length()==0)
        {
            showLongToast("Enter name!");
            return;
        }
        girviTransaction.setName(name.getText().toString());

        if(fathersName.getText().length()==0)
        {
            showLongToast("Enter Father's name!");
            return;
        }
        girviTransaction.setFatherName(fathersName.getText().toString());

        if(villageName.getText().length()==0)
        {
            showLongToast("Enter Village name!");
            return;
        }
        girviTransaction.setVillage(villageName.getText().toString());

        if(caste.getText().length()==0)
        {
            showLongToast("Enter Caste!");
            return;
        }
        girviTransaction.setCaste(caste.getText().toString());

        if(mobileNumber.getText().length()==0)
        {
            showLongToast("Enter Mobile Number!");
            return;
        }
        girviTransaction.setPhoneNumber(mobileNumber.getText().toString());

        if(metalType.getText().length()==0)
        {
            showLongToast("Enter Metal Type!");
            return;
        }
        girviTransaction.setMetalType(MetalType.getIndexFromType(metalType.getText().toString()));

        if(itemName.getText().length()==0)
        {
            showLongToast("Enter Item Name!");
            return;
        }
        girviTransaction.setItemName(itemName.getText().toString());

        if(itemWeight.getText().length()==0)
        {
            showLongToast("Enter Item Weight!");
            return;
        }
        girviTransaction.setWeight(Double.parseDouble(itemWeight.getText().toString()));

        if(itemTunch.getText().length()==0)
        {
            showLongToast("Enter Tunch!");
            return;
        }
        girviTransaction.setTunch(Double.parseDouble(itemTunch.getText().toString()));

        if(interestPercentage.getText().length()==0)
        {
            showLongToast("Enter Interest!");
            return;
        }
        girviTransaction.setInterestPercentage(InterestRates.getIndexFromRate(interestPercentage.getText().toString()));

        girviTransaction.setMetalPrice(Double.parseDouble(metaldRateEditText.getText().toString()));
        girviTransaction.setCalcValue(Double.parseDouble(valueCalculated.getText().toString()));

        if(moneyLoaned.getText().length()==0)
        {
            showLongToast("Enter the Loan Amount");
            return;
        }
        girviTransaction.setMoneyGiven(Double.parseDouble(moneyLoaned.getText().toString()));
        girviTransaction.setSettled(1);
        insertAndQueryDb.insertGirviTransaction(modelToContentValue.girviTransactionToContentValue(girviTransaction));
        showLongToast("Transaction Created");
        setResult(RESULT_OK);
        finish();
    }
}
