package com.badjatya.ledger.Database.CursorWrappers;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.badjatya.ledger.Database.DatabaseSchema;
import com.badjatya.ledger.models.Village;

/**
 * Created by Tanmay on 11/1/2016.
 */
public class VillageCursorWrapper extends CursorWrapper {

    public VillageCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public Village getVillage()
    {
        String uuid=getString(getColumnIndex(DatabaseSchema.VillageTable.Cols.UUID));
        String villageName=getString(getColumnIndex(DatabaseSchema.VillageTable.Cols.VILLAGE_NAME));
        return new Village(uuid,villageName);
    }
}
