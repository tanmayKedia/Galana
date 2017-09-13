package com.badjatya.ledger.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import com.badjatya.ledger.Database.CursorWrappers.TransactionCursorWrapper;
import com.badjatya.ledger.Database.DatabaseHelper;
import com.badjatya.ledger.Database.InsertAndQueryDb;
import com.badjatya.ledger.Database.ModelToContentValue;
import com.badjatya.ledger.models.GirviTransaction;
import com.badjatya.ledger.models.InterestRates;
import com.badjatya.ledger.models.MetalType;
import com.badjatya.ledger.models.Transaction;
import com.badjatya.ledger.Database.DatabaseSchema.TransactionTable;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by Tanmay on 11/18/2016.
 */
public class GirviTransactionEvaluator
{
    Context context;
    public GirviTransactionEvaluator(Context context)
    {
        this.context=context;
    }
    private static final String PREF_GOLD_RATE="gold_rate";
    private static final String PREF_SILVER_RATE="silver_rate";

    public boolean isReadyToMelt(GirviTransaction girviTransaction)
    {
        logMe("isReadyToMelt called");
        logMe("previous price was "+girviTransaction.getMetalPrice());
        logMe("previous value was "+girviTransaction.getCalcValue());
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        Double goldRate = Double.longBitsToDouble(pref.getLong(PREF_GOLD_RATE, 0));
        Double silverRate = Double.longBitsToDouble(pref.getLong(PREF_SILVER_RATE, 0));
        Double metalRate;
        if(girviTransaction.getMetalType()==0)
        {
            metalRate=goldRate/10;
        }
        else
        {
            metalRate=silverRate/1000;
        }
        logMe("current price is= "+metalRate);
        logMe("weight is= "+ girviTransaction.getWeight());
        logMe("tunch/100 is"+ girviTransaction.getTunch()/100);
        double currentValue=(girviTransaction.getWeight()*(girviTransaction.getTunch()/100)*metalRate);
        logMe("current value is= "+currentValue);
        double settleAmount= settleAmount(girviTransaction);

        double delta=currentValue-settleAmount;
        logMe("isReadyToMelt END");
        if(delta>0)
        {
            return false;
        }
        else
        {
            return true;
        }

    }
    public double currentValue(GirviTransaction girviTransaction)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        Double goldRate = Double.longBitsToDouble(pref.getLong(PREF_GOLD_RATE, 0));
        Double silverRate = Double.longBitsToDouble(pref.getLong(PREF_SILVER_RATE, 0));
        Double metalRate;
        if(girviTransaction.getMetalType()==0)
        {
            metalRate=goldRate/10;
        }
        else
        {
            metalRate=silverRate/1000;
        }
        double currentValue=(girviTransaction.getWeight()*(girviTransaction.getTunch()/100)*metalRate);
        return currentValue;
    }
    public double settleAmount(GirviTransaction girviTransaction)
    {
        logMe("settle amount called");
        SQLiteDatabase sqLiteDatabase;
        InsertAndQueryDb insertAndQueryDb;
        sqLiteDatabase= new DatabaseHelper(context).getWritableDatabase();
        insertAndQueryDb =new InsertAndQueryDb(sqLiteDatabase);

        double settleAmount=calculateCompoundAmount(girviTransaction.getMoneyGiven(), InterestRates.getDoubleRateFromIndex(girviTransaction.getInterestPercentage()),girviTransaction.getDate());
        logMe("for gt "+girviTransaction.toString());
        logMe("Settle Amount after principle cI calc= "+settleAmount);

        ArrayList<Transaction> transactions=new ArrayList<Transaction>();
        TransactionCursorWrapper tc=insertAndQueryDb.queryTransaction(TransactionTable.Cols.GIRVI_TRANSACTION_ID+"=?",new String[]{girviTransaction.getId()});
        try
        {
            tc.moveToFirst();

            while (tc.isAfterLast()!=true)
            {
                transactions.add(tc.getTransaction());
                tc.moveToNext();
            }
        }
        finally {
         tc.close();
        }
        for (Transaction t:transactions)
        {
            settleAmount=settleAmount-calculateCompoundAmount(t.getAmount(), InterestRates.getDoubleRateFromIndex(girviTransaction.getInterestPercentage()),t.getDate());
            logMe("Settle Amount after transaction"+t.toString()+"  "+settleAmount);
        }
        logMe("final Settle Amount is= "+settleAmount);
        logMe("settle amount END");
        return settleAmount;
    }

    private double calculateCompoundAmount(double principle,double monthRate,long startEpoch)
    {
        logMe("calculate compound interest called");

        logMe("priciple "+principle+" rate= "+monthRate*12);
        logMe("no of days days "+Days.daysBetween(new LocalDate(startEpoch),LocalDate.now()).getDays());
        double years= (double)Days.daysBetween(new LocalDate(startEpoch), LocalDate.now()).getDays()/365;
        logMe("years = " + years);
        double yearlyRate=12*monthRate;
        double fractionalYear=years%1;
        logMe("fractionyear= "+fractionalYear);
        double wholeYear=years-fractionalYear;
        logMe("wholeYear " +wholeYear);
        double secondTerm=Math.pow((1+(yearlyRate/100)),wholeYear);
        double thirdTerm=(1+((yearlyRate/100)*fractionalYear));
        logMe("second term "+ secondTerm);
        logMe("third term"+ thirdTerm);
        double amount=principle*secondTerm*thirdTerm;
        logMe("calulated amount wit ci is"+amount);
        logMe("calculate compound interest END");
        return amount;
    }

    void logMe(String message)
    {
        Log.d("Voldemort",message);
    }
}
