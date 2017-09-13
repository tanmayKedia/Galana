package com.badjatya.ledger.Database.CursorWrappers;

import android.database.Cursor;

import com.badjatya.ledger.Database.DatabaseSchema;
import com.badjatya.ledger.Rakam.RakamTransaction;

/**
 * Created by Tanmay on 11/29/2016.
 */
public class RakamTransactionCursorWrapper extends CasteCursorWrapper {

    public RakamTransactionCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public RakamTransaction getRakamTransaction()
    {

        String uuid=getString(getColumnIndex(DatabaseSchema.RakamTransactionTable.Cols.UUID));
        String name=getString(getColumnIndex(DatabaseSchema.RakamTransactionTable.Cols.NAME));
        String fName=getString(getColumnIndex(DatabaseSchema.RakamTransactionTable.Cols.FATHER_NAME));
        String village=getString(getColumnIndex(DatabaseSchema.RakamTransactionTable.Cols.VILLAGE));
        String caste=getString(getColumnIndex(DatabaseSchema.RakamTransactionTable.Cols.CASTE));
        String phone=getString(getColumnIndex(DatabaseSchema.RakamTransactionTable.Cols.PHONE_NUMBERS));
        long date=getLong(getColumnIndex(DatabaseSchema.RakamTransactionTable.Cols.DATE));
        String itemName=getString(getColumnIndex(DatabaseSchema.RakamTransactionTable.Cols.ITEM_NAME));
        int interest=getInt(getColumnIndex(DatabaseSchema.RakamTransactionTable.Cols.INTEREST));
        double itemValue = getDouble(getColumnIndex(DatabaseSchema.RakamTransactionTable.Cols.ITEM_VALUE));
        double moneyGiven = getDouble(getColumnIndex(DatabaseSchema.RakamTransactionTable.Cols.MONEY_LOANED));
        int settled=getInt(getColumnIndex(DatabaseSchema.RakamTransactionTable.Cols.SETTLE_STATUS));

        return new RakamTransaction(uuid,
                date,
                name,
                fName,
                village,
                caste,
                phone,
                itemName,
                interest,
                itemValue,
                moneyGiven,
                settled
        );
    }
}
