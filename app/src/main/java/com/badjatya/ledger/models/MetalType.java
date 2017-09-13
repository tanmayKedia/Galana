package com.badjatya.ledger.models;

/**
 * Created by Tanmay on 10/29/2016.
 */
public class MetalType {

    private static String[] metalArray={"Gold","Silver"};

    public static String[] getMetalArray() {
        return metalArray;
    }

    public static int getIndexFromType(String type)
    {   int index=-1;
        for(int i=0;i<metalArray.length;i++)
        {
            if(type.equals(metalArray[i]))
            {
                index=i;
                break;
            }
        }
        return index;
    }

    public static String getTypeFromIndex(int index)
    {
        return metalArray[index];
    }
}
