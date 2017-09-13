package com.badjatya.ledger.Database.CursorWrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.badjatya.ledger.Database.DatabaseSchema;
import com.badjatya.ledger.models.Caste;

/**
 * Created by Tanmay on 11/1/2016.
 */
public class CasteCursorWrapper extends CursorWrapper {

    public CasteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Caste getCaste()
    {
        String uuid=getString(getColumnIndex(DatabaseSchema.CasteTable.Cols.UUID));
        String casteName=getString(getColumnIndex(DatabaseSchema.CasteTable.Cols.CASTE_NAME));
        return new Caste(uuid,casteName);
    }
}
