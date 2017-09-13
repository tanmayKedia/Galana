package com.badjatya.ledger.Rakam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.badjatya.ledger.Database.CursorWrappers.TransactionCursorWrapper;
import com.badjatya.ledger.Database.DatabaseHelper;
import com.badjatya.ledger.Database.DatabaseSchema;
import com.badjatya.ledger.Database.InsertAndQueryDb;
import com.badjatya.ledger.models.GirviTransaction;
import com.badjatya.ledger.models.InterestRates;
import com.badjatya.ledger.models.Transaction;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by Tanmay on 11/30/2016.
 */
public class RakamTransactionEvaluator {


    Context context;
    public RakamTransactionEvaluator(Context context)
    {
        this.context=context;
    }

    public double settleAmount(RakamTransaction rakamTransaction)
    {
        logMe("settle amount called");
        SQLiteDatabase sqLiteDatabase;
        InsertAndQueryDb insertAndQueryDb;
        sqLiteDatabase= new DatabaseHelper(context).getWritableDatabase();
        insertAndQueryDb =new InsertAndQueryDb(sqLiteDatabase);

        double settleAmount=calculateCompoundAmount(rakamTransaction.getMoneyGiven(), InterestRates.getDoubleRateFromIndex(rakamTransaction.getInterestPercentage()),rakamTransaction.getDate());
        logMe("for gt " + rakamTransaction.toString());
        logMe("Settle Amount after principle cI calc= "+settleAmount);

        ArrayList<Transaction> transactions=new ArrayList<Transaction>();
        TransactionCursorWrapper tc=insertAndQueryDb.queryTransaction(DatabaseSchema.TransactionTable.Cols.GIRVI_TRANSACTION_ID+"=?",new String[]{rakamTransaction.getId()});
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
            settleAmount=settleAmount-calculateCompoundAmount(t.getAmount(), InterestRates.getDoubleRateFromIndex(rakamTransaction.getInterestPercentage()),t.getDate());
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
        logMe("no of days days "+ Days.daysBetween(new LocalDate(startEpoch), LocalDate.now()).getDays());
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
        Log.d("Voldemort", message);
    }
}
