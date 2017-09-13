package com.badjatya.ledger.Rakam.RakamList;

import com.badjatya.ledger.Rakam.RakamTransaction;
import com.badjatya.ledger.models.GirviFilter;
import com.badjatya.ledger.models.GirviTransaction;

import java.util.ArrayList;

/**
 * Created by Tanmay on 11/30/2016.
 */
public class RakamTransactionFilter {

    public ArrayList<RakamTransaction> filterRakamTransactions(GirviFilter girviFilter,ArrayList<RakamTransaction> originalList)
    {
        ArrayList<RakamTransaction> filteredList=new ArrayList<RakamTransaction>();
        ArrayList<RakamTransaction> tempList=new ArrayList<RakamTransaction>();
        filteredList=(ArrayList)originalList.clone();

        if(girviFilter.getName()!=null&&girviFilter.getName().length()!=0)
        {
            for(RakamTransaction rt:filteredList)
            {
                if(rt.getName().startsWith(girviFilter.getName()))
                {
                    tempList.add(rt);
                }
            }
            filteredList.clear();
            filteredList=(ArrayList)tempList.clone();
            tempList.clear();
        }
        if(girviFilter.getFatherName()!=null&&girviFilter.getFatherName().length()!=0)
        {
            for (RakamTransaction rt:filteredList)
            {
                if(rt.getFatherName().startsWith(girviFilter.getFatherName()))
                {
                    tempList.add(rt);
                }
            }
            filteredList.clear();
            filteredList=(ArrayList)tempList.clone();
            tempList.clear();
        }

        if(girviFilter.getVillage()!=null&&girviFilter.getVillage().length()!=0)
        {
            for (RakamTransaction rt:filteredList)
            {
                if(rt.getVillage().equals(girviFilter.getVillage()))
                {
                    tempList.add(rt);
                }
            }
            filteredList.clear();
            filteredList=(ArrayList)tempList.clone();
            tempList.clear();
        }
        if(girviFilter.getCaste()!=null&&girviFilter.getCaste().length()!=0)
        {
            for (RakamTransaction rt:filteredList)
            {
                if(rt.getCaste().equals(girviFilter.getCaste()))
                {
                    tempList.add(rt);
                }
            }
            filteredList.clear();
            filteredList=(ArrayList)tempList.clone();
            tempList.clear();
        }
        return filteredList;
    }
}
