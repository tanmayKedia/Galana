package com.badjatya.ledger.Database.CursorWrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.badjatya.ledger.Database.DatabaseSchema.TransactionTable;
import com.badjatya.ledger.models.Transaction;

/**
 * Created by Tanmay on 11/10/2016.
 */
public class TransactionCursorWrapper extends CursorWrapper{

    public TransactionCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Transaction getTransaction()
    {
        String uuid=getString(getColumnIndex(TransactionTable.Cols.ID));
        String gtId=getString(getColumnIndex(TransactionTable.Cols.GIRVI_TRANSACTION_ID));
        long date=getLong(getColumnIndex(TransactionTable.Cols.DATE));
        Double amount=getDouble(getColumnIndex(TransactionTable.Cols.AMOUNT));
        return new Transaction(uuid,gtId,amount,date);
    }
}
