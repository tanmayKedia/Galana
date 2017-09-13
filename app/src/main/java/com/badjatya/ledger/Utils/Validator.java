package com.badjatya.ledger.Utils;

/**
 * Created by Tanmay on 10/29/2016.
 */
public class Validator {

   public static boolean isParasableDouble(String number)
    {
        try
        {
                double d=Double.parseDouble(number);
                return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public static boolean isParsableInt(String number)
    {
        try
        {
            int i=Integer.parseInt(number);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
