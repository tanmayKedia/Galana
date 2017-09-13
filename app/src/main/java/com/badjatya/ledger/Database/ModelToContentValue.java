package com.badjatya.ledger.Database;

import android.content.ContentValues;

import com.badjatya.ledger.Rakam.RakamTransaction;
import com.badjatya.ledger.models.Caste;
import com.badjatya.ledger.models.GirviTransaction;
import com.badjatya.ledger.models.Transaction;
import com.badjatya.ledger.models.Village;
import com.badjatya.ledger.Database.DatabaseSchema.VillageTable;
import com.badjatya.ledger.Database.DatabaseSchema.GirviTransactionTable;
import com.badjatya.ledger.Database.DatabaseSchema.TransactionTable;
import com.badjatya.ledger.Database.DatabaseSchema.RakamTransactionTable;

/**
 * Created by Tanmay on 11/1/2016.
 */
public class ModelToContentValue {

    public ContentValues villageToContentValue(Village village)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(VillageTable.Cols.UUID, village.getId());
        contentValues.put(VillageTable.Cols.VILLAGE_NAME,village.getVillageName());
        return contentValues;
    }

    public ContentValues casteToContentValue(Caste caste)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(DatabaseSchema.CasteTable.Cols.UUID, caste.getId());
        contentValues.put(DatabaseSchema.CasteTable.Cols.CASTE_NAME,caste.getCasteName());
        return contentValues;
    }

    public ContentValues girviTransactionToContentValue(GirviTransaction girviTransaction)
    {
      ContentValues contentValues=new ContentValues();
        contentValues.put(GirviTransactionTable.Cols.UUID,girviTransaction.getId());
        contentValues.put(GirviTransactionTable.Cols.NAME,girviTransaction.getName());
        contentValues.put(GirviTransactionTable.Cols.FATHER_NAME,girviTransaction.getFatherName());
        contentValues.put(GirviTransactionTable.Cols.VILLAGE,girviTransaction.getVillage());
        contentValues.put(GirviTransactionTable.Cols.CASTE,girviTransaction.getCaste());
        contentValues.put(GirviTransactionTable.Cols.PHONE_NUMBERS,girviTransaction.getPhoneNumber());
        contentValues.put(GirviTransactionTable.Cols.DATE,girviTransaction.getDate());
        contentValues.put(GirviTransactionTable.Cols.ITEM_NAME,girviTransaction.getItemName());
        contentValues.put(GirviTransactionTable.Cols.METAL_TYPE,girviTransaction.getMetalType());
        contentValues.put(GirviTransactionTable.Cols.INTEREST,girviTransaction.getInterestPercentage());
        contentValues.put(GirviTransactionTable.Cols.WEIGHT,girviTransaction.getWeight());
        contentValues.put(GirviTransactionTable.Cols.TUNCH,girviTransaction.getTunch());
        contentValues.put(GirviTransactionTable.Cols.METAL_PRICE,girviTransaction.getMetalPrice());
        contentValues.put(GirviTransactionTable.Cols.VALUE_CALC,girviTransaction.getCalcValue());
        contentValues.put(GirviTransactionTable.Cols.MONEY_LOANED,girviTransaction.getMoneyGiven());
        contentValues.put(GirviTransactionTable.Cols.SETTLE_STATUS,girviTransaction.getSettled());
        return contentValues;
    }

    public ContentValues transactionToContentValue(Transaction transaction)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(TransactionTable.Cols.ID,transaction.getId());
        contentValues.put(TransactionTable.Cols.GIRVI_TRANSACTION_ID,transaction.getGirviTransactionId());
        contentValues.put(TransactionTable.Cols.DATE,transaction.getDate());
        contentValues.put(TransactionTable.Cols.AMOUNT,transaction.getAmount());
        return contentValues;
    }

    public ContentValues rakamTransactionToContentValues(RakamTransaction rakamTransaction)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(RakamTransactionTable.Cols.UUID,rakamTransaction.getId());
        contentValues.put(RakamTransactionTable.Cols.NAME,rakamTransaction.getName());
        contentValues.put(RakamTransactionTable.Cols.FATHER_NAME,rakamTransaction.getFatherName());
        contentValues.put(RakamTransactionTable.Cols.VILLAGE,rakamTransaction.getVillage());
        contentValues.put(RakamTransactionTable.Cols.CASTE,rakamTransaction.getCaste());
        contentValues.put(RakamTransactionTable.Cols.PHONE_NUMBERS,rakamTransaction.getPhoneNumber());
        contentValues.put(RakamTransactionTable.Cols.DATE,rakamTransaction.getDate());
        contentValues.put(RakamTransactionTable.Cols.ITEM_NAME,rakamTransaction.getItemName());
        contentValues.put(RakamTransactionTable.Cols.INTEREST,rakamTransaction.getInterestPercentage());
        contentValues.put(RakamTransactionTable.Cols.ITEM_VALUE,rakamTransaction.getItemValue());
        contentValues.put(RakamTransactionTable.Cols.MONEY_LOANED,rakamTransaction.getMoneyGiven());
        contentValues.put(RakamTransactionTable.Cols.SETTLE_STATUS,rakamTransaction.getSettled());
        return contentValues;
    }
}
