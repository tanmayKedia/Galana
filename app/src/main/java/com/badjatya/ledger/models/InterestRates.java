package com.badjatya.ledger.models;

/**
 * Created by Tanmay on 10/29/2016.
 */
public class InterestRates {

    private static String[] ratesArray={"1.5%","1.75%","2%"};
    private static double[] doubleRatesArray={1.5,1.75,2.0};

    public static String[] getRatesArray() {
        return ratesArray;
    }

    public static int getIndexFromRate(String rate)
    {   int index=-1;
        for(int i=0;i<ratesArray.length;i++)
        {
            if(rate.equals(ratesArray[i]))
            {
                index=i;
                break;
            }
        }
        return index;
    }

    public static String getRateFormIndex(int index)
    {
        return ratesArray[index];
    }
    public static double getDoubleRateFromIndex(int index)
    {
        return doubleRatesArray[index];
    }
}
