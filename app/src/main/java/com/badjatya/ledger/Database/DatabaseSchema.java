package com.badjatya.ledger.Database;

/**
 * Created by Tanmay on 11/1/2016.
 */
public class DatabaseSchema {

    //village table
    public static final class VillageTable
    {
        public static final String NAME="villagetable";

        public static final class Cols
        {
            public static final String UUID ="id";
            public static final String VILLAGE_NAME="villagename";
        }
    }

    //caste table
    public static final class CasteTable
    {
        public static final String NAME="castetable";

        public static final class Cols
        {
            public static final String UUID ="id";
            public static final String CASTE_NAME="castename";
        }
    }

    public static final class GirviTransactionTable
    {
        public static final String NAME="girvitransactiontable";

        public static class Cols
        {

            public static final String UUID="id";
            public static final String NAME="name";
            public static final String FATHER_NAME="fname";
            public static final String VILLAGE="village";
            public static final String CASTE="caste";
            public static final String PHONE_NUMBERS="ph_no";
            public static final String DATE="date";
            public static final String ITEM_NAME="item_name";
            public static final String METAL_TYPE="m_type";
            public static final String INTEREST="i_percent";
            public static final String WEIGHT="weight";
            public static final String TUNCH="tunch";
            public static final String METAL_PRICE="metal_price";
            public static final String VALUE_CALC="value_calc";
            public static final String MONEY_LOANED="money_loaned";
            public static final String SETTLE_STATUS="settle_status";


        }
    }

    public static final class TransactionTable {
        public static final String NAME = "transactiontable";

        public static class Cols {
            public static final String ID = "ID";
            public static final String GIRVI_TRANSACTION_ID = "G_TRAN_ID";
            public static final String DATE = "TRAN_DATE";
            public static final String AMOUNT = "TRAN_AMOUNT";
        }
    }

    public static final class RakamTransactionTable
    {
        public static final String NAME = "rakamtransactiontable";

        public static class Cols
        {
            public static final String UUID = "id";
            public static final String NAME = "name";
            public static final String FATHER_NAME = "fname";
            public static final String VILLAGE = "village";
            public static final String CASTE = "caste";
            public static final String PHONE_NUMBERS = "ph_no";
            public static final String DATE = "date";
            public static final String ITEM_NAME = "item_name";
            public static final String INTEREST="i_percent";
            public static final String ITEM_VALUE="item_value";
            public static final String MONEY_LOANED="money_loaned";
            public static final String SETTLE_STATUS="settle_status";
        }
    }
}
