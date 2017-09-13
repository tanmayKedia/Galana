package com.badjatya.ledger.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.badjatya.ledger.Database.DatabaseSchema.VillageTable;
import com.badjatya.ledger.Database.DatabaseSchema.CasteTable;
import com.badjatya.ledger.Database.DatabaseSchema.GirviTransactionTable;
import com.badjatya.ledger.Database.DatabaseSchema.TransactionTable;
import com.badjatya.ledger.Rakam.RakamTransaction;
import com.badjatya.ledger.models.Transaction;
import com.badjatya.ledger.Database.DatabaseSchema.RakamTransactionTable;


/**
 * Created by Tanmay on 11/1/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION=1;
    private static final String DATABASE_NAME="galana.db";

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table "+ DatabaseSchema.VillageTable.NAME+ "("
                +" _id integer primary key autoincrement,"
                + VillageTable.Cols.UUID +","
                +VillageTable.Cols.VILLAGE_NAME+" UNIQUE"
                +")"
        );

        db.execSQL(
                "create table "+ DatabaseSchema.CasteTable.NAME+ "("
                        +" _id integer primary key autoincrement,"
                        + CasteTable.Cols.UUID +","
                        +CasteTable.Cols.CASTE_NAME+" UNIQUE"
                        +")"
        );

        db.execSQL(
                "create table "+ DatabaseSchema.GirviTransactionTable.NAME +"("
                    + GirviTransactionTable.Cols.UUID+" primary key,"
                        + GirviTransactionTable.Cols.NAME+","
                        + GirviTransactionTable.Cols.FATHER_NAME+","
                        + GirviTransactionTable.Cols.VILLAGE+","
                        + GirviTransactionTable.Cols.CASTE+","
                        + GirviTransactionTable.Cols.PHONE_NUMBERS+","
                        + GirviTransactionTable.Cols.DATE+","
                        + GirviTransactionTable.Cols.ITEM_NAME+","
                        + GirviTransactionTable.Cols.METAL_TYPE+","
                        + GirviTransactionTable.Cols.INTEREST+","
                        + GirviTransactionTable.Cols.WEIGHT+","
                        + GirviTransactionTable.Cols.TUNCH+","
                        + GirviTransactionTable.Cols.METAL_PRICE+","
                        + GirviTransactionTable.Cols.VALUE_CALC+","
                        + GirviTransactionTable.Cols.MONEY_LOANED+","
                        + GirviTransactionTable.Cols.SETTLE_STATUS+")"
        );

        db.execSQL(
                "create table "+ TransactionTable.NAME +"("
                        + TransactionTable.Cols.ID+" primary key,"
                        + TransactionTable.Cols.GIRVI_TRANSACTION_ID+","
                        + TransactionTable.Cols.DATE+","
                        + TransactionTable.Cols.AMOUNT+","
                        + " FOREIGN KEY ("+TransactionTable.Cols.GIRVI_TRANSACTION_ID+") REFERENCES "+GirviTransactionTable.NAME+"("+GirviTransactionTable.Cols.UUID+")"+")"
        );

        db.execSQL(
                "create table "+ DatabaseSchema.RakamTransactionTable.NAME +"("
                        + RakamTransactionTable.Cols.UUID+" primary key,"
                        + RakamTransactionTable.Cols.NAME+","
                        + RakamTransactionTable.Cols.FATHER_NAME+","
                        + RakamTransactionTable.Cols.VILLAGE+","
                        + RakamTransactionTable.Cols.CASTE+","
                        + RakamTransactionTable.Cols.PHONE_NUMBERS+","
                        + RakamTransactionTable.Cols.DATE+","
                        + RakamTransactionTable.Cols.ITEM_NAME+","
                        + RakamTransactionTable.Cols.INTEREST+","
                        + RakamTransactionTable.Cols.ITEM_VALUE+","
                        + RakamTransactionTable.Cols.MONEY_LOANED+","
                        + RakamTransactionTable.Cols.SETTLE_STATUS+")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
