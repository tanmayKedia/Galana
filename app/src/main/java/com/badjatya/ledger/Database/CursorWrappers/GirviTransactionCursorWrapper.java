package com.badjatya.ledger.Database.CursorWrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.badjatya.ledger.Database.DatabaseSchema.GirviTransactionTable;
import com.badjatya.ledger.models.GirviTransaction;

import java.util.UUID;

/**
 * Created by Tanmay on 11/6/2016.
 */
public class GirviTransactionCursorWrapper extends CursorWrapper {

    public GirviTransactionCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public GirviTransaction getGirviTransaction()
    {
        String uuid=getString(getColumnIndex(GirviTransactionTable.Cols.UUID));
        String name=getString(getColumnIndex(GirviTransactionTable.Cols.NAME));
        String fName=getString(getColumnIndex(GirviTransactionTable.Cols.FATHER_NAME));
        String village=getString(getColumnIndex(GirviTransactionTable.Cols.VILLAGE));
        String caste=getString(getColumnIndex(GirviTransactionTable.Cols.CASTE));
        String phone=getString(getColumnIndex(GirviTransactionTable.Cols.PHONE_NUMBERS));
        long date=getLong(getColumnIndex(GirviTransactionTable.Cols.DATE));
        String itemName=getString(getColumnIndex(GirviTransactionTable.Cols.ITEM_NAME));
        int metalType=getInt(getColumnIndex(GirviTransactionTable.Cols.METAL_TYPE));
        int interest=getInt(getColumnIndex(GirviTransactionTable.Cols.INTEREST));
        double weight=getDouble(getColumnIndex(GirviTransactionTable.Cols.WEIGHT));
        double tunch=getDouble(getColumnIndex(GirviTransactionTable.Cols.TUNCH));
        double metalPrice = getDouble(getColumnIndex(GirviTransactionTable.Cols.METAL_PRICE));
        double calcValue = getDouble(getColumnIndex(GirviTransactionTable.Cols.VALUE_CALC));
        double moneyGiven = getDouble(getColumnIndex(GirviTransactionTable.Cols.MONEY_LOANED));
        int settled=getInt(getColumnIndex(GirviTransactionTable.Cols.SETTLE_STATUS));

        return new GirviTransaction(uuid,
                name,
                fName,
                village,
                caste,
                phone,
                date,
                itemName,
                metalType,
                interest,
                weight,
                tunch,
                metalPrice,
                calcValue,
                moneyGiven,
                settled);
    }
}
