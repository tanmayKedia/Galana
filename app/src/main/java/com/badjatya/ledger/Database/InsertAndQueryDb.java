package com.badjatya.ledger.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.badjatya.ledger.Database.CursorWrappers.CasteCursorWrapper;
import com.badjatya.ledger.Database.CursorWrappers.GirviTransactionCursorWrapper;
import com.badjatya.ledger.Database.CursorWrappers.RakamTransactionCursorWrapper;
import com.badjatya.ledger.Database.CursorWrappers.TransactionCursorWrapper;
import com.badjatya.ledger.Database.CursorWrappers.VillageCursorWrapper;
import com.badjatya.ledger.models.Caste;

/**
 * Created by Tanmay on 11/1/2016.
 */
public class InsertAndQueryDb {

    SQLiteDatabase sqLiteDatabase;

    public InsertAndQueryDb(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void insertVillage(ContentValues values)
    {
        sqLiteDatabase.insert(DatabaseSchema.VillageTable.NAME,null,values);
    }

    public void insertCaste(ContentValues values)
    {
        sqLiteDatabase.insert(DatabaseSchema.CasteTable.NAME,null,values);
    }

    public void insertGirviTransaction(ContentValues values)
    {
        sqLiteDatabase.insert(DatabaseSchema.GirviTransactionTable.NAME,null,values);
    }

    public void insertTransaction(ContentValues values)
    {
        sqLiteDatabase.insert(DatabaseSchema.TransactionTable.NAME,null,values);
    }

    public void insertRakamTransaction(ContentValues values)
    {
        sqLiteDatabase.insert(DatabaseSchema.RakamTransactionTable.NAME,null,values);
    }

    public GirviTransactionCursorWrapper queryGirviTransactionTable(String whereClause,String[] whereArgs)
    {

        Cursor cursor= sqLiteDatabase.query(
                DatabaseSchema.GirviTransactionTable.NAME,
                null
                ,whereClause
                ,whereArgs
                ,null
                ,null
                ,null);
        return new GirviTransactionCursorWrapper(cursor);

    }
    public RakamTransactionCursorWrapper queryRakamTransactionTable(String whereClause,String[] whereArgs)
    {
        Cursor cursor=sqLiteDatabase.query(
                DatabaseSchema.RakamTransactionTable.NAME,
                null
                ,whereClause
                ,whereArgs
                ,null
                ,null
                ,null);
        return new RakamTransactionCursorWrapper(cursor);
    }

    public VillageCursorWrapper queryVillage(String whereClause,String[] whereArgs)
    {
        Cursor cursor= sqLiteDatabase.query(
                DatabaseSchema.VillageTable.NAME,
                null
                ,whereClause
                ,whereArgs
                ,null
                ,null
                ,null);

        return new VillageCursorWrapper(cursor);
    }

    public CasteCursorWrapper queryCaste(String whereClause,String[] whereArgs)
    {
        Cursor cursor= sqLiteDatabase.query(
                DatabaseSchema.CasteTable.NAME,
                null
                ,whereClause
                ,whereArgs
                ,null
                ,null
                ,null);
        return new CasteCursorWrapper(cursor);
    }

    public TransactionCursorWrapper queryTransaction(String whereClause,String[] whereArgs)
    {
        Cursor cursor= sqLiteDatabase.query(
                DatabaseSchema.TransactionTable.NAME,
                null
                ,whereClause
                ,whereArgs
                ,null
                ,null
                ,null);
        return new TransactionCursorWrapper(cursor);
    }

    public void deleteTransactions(String where,String[] whereArgs)
    {
        sqLiteDatabase.delete(DatabaseSchema.TransactionTable.NAME,where,whereArgs);
    }
    public void deleteGirviTransaction(String where,String[] whereArgs)
    {
        sqLiteDatabase.delete(DatabaseSchema.GirviTransactionTable.NAME,where,whereArgs);
    }

    public void deleteRakamTransaction(String where,String[] whereArgs)
    {
        sqLiteDatabase.delete(DatabaseSchema.RakamTransactionTable.NAME,where,whereArgs);
    }

    public void updateGirviTransaction(ContentValues contentValues,String where,String[] whereArgs)
    {
        sqLiteDatabase.update(DatabaseSchema.GirviTransactionTable.NAME, contentValues, where, whereArgs);
    }

    public void updateRakamTransaction(ContentValues contentValues,String where,String[] whereArgs)
    {
        sqLiteDatabase.update(DatabaseSchema.RakamTransactionTable.NAME,contentValues,where,whereArgs);
    }
}
