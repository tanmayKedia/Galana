package com.badjatya.ledger.Utils;

import com.badjatya.ledger.models.GirviFilter;
import com.badjatya.ledger.models.GirviTransaction;

import java.util.ArrayList;

/**
 * Created by Tanmay on 11/14/2016.
 */
public class GirviTransactionFilter {
    //Filters original list according to the given transaction list
    //A new ArrayList is returned originalList in NOT modified in anyway
    public ArrayList<GirviTransaction> filterGirviTransactions(GirviFilter girviFilter,ArrayList<GirviTransaction> originalList)
    {
        ArrayList<GirviTransaction> filteredList=new ArrayList<GirviTransaction>();
        ArrayList<GirviTransaction> tempList=new ArrayList<GirviTransaction>();
        filteredList=(ArrayList)originalList.clone();
        if(girviFilter.getName()!=null&&girviFilter.getName().length()!=0)
        {
            for(GirviTransaction gt:filteredList)
            {
                if(gt.getName().startsWith(girviFilter.getName()))
                {
                    tempList.add(gt);
                }
            }
            filteredList.clear();
            filteredList=(ArrayList)tempList.clone();
            tempList.clear();
        }
        if(girviFilter.getFatherName()!=null&&girviFilter.getFatherName().length()!=0)
        {
            for (GirviTransaction gt:filteredList)
            {
                if(gt.getFatherName().startsWith(girviFilter.getFatherName()))
                {
                    tempList.add(gt);
                }
            }
            filteredList.clear();
            filteredList=(ArrayList)tempList.clone();
            tempList.clear();
        }

        if(girviFilter.getVillage()!=null&&girviFilter.getVillage().length()!=0)
        {
            for (GirviTransaction gt:filteredList)
            {
                if(gt.getVillage().equals(girviFilter.getVillage()))
                {
                    tempList.add(gt);
                }
            }
            filteredList.clear();
            filteredList=(ArrayList)tempList.clone();
            tempList.clear();
        }
        if(girviFilter.getCaste()!=null&&girviFilter.getCaste().length()!=0)
        {
            for (GirviTransaction gt:filteredList)
            {
                if(gt.getCaste().equals(girviFilter.getCaste()))
                {
                    tempList.add(gt);
                }
            }
            filteredList.clear();
            filteredList=(ArrayList)tempList.clone();
            tempList.clear();
        }
        return filteredList;
    }
}


