package com.badjatya.ledger.Rakam;

import java.util.UUID;

/**
 * Created by Tanmay on 11/28/2016.
 */
public class RakamTransaction {

    String id;
    long date;
    String name;
    String fatherName;
    String village;
    String Caste;
    String phoneNumber;
    String itemName;
    int interestPercentage; //0-->1.5% 1-->1.75% 2-->2.0%
    double itemValue;
    double moneyGiven;
    int settled; //1 MEANS OPEN, 2 MEANS SETTLED

    public RakamTransaction(String id, long date, String name, String fatherName, String village, String caste, String phoneNumber, String itemName, int interestPercentage, double itemValue, double moneyGiven, int settled) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.fatherName = fatherName;
        this.village = village;
        Caste = caste;
        this.phoneNumber = phoneNumber;
        this.itemName = itemName;
        this.interestPercentage = interestPercentage;
        this.itemValue = itemValue;
        this.moneyGiven = moneyGiven;
        this.settled = settled;
    }

    public RakamTransaction()
    {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getCaste() {
        return Caste;
    }

    public void setCaste(String caste) {
        Caste = caste;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getInterestPercentage() {
        return interestPercentage;
    }

    public void setInterestPercentage(int interestPercentage) {
        this.interestPercentage = interestPercentage;
    }

    public double getItemValue() {
        return itemValue;
    }

    public void setItemValue(double itemValue) {
        this.itemValue = itemValue;
    }

    public double getMoneyGiven() {
        return moneyGiven;
    }

    public void setMoneyGiven(double moneyGiven) {
        this.moneyGiven = moneyGiven;
    }

    public int getSettled() {
        return settled;
    }

    public void setSettled(int settled) {
        this.settled = settled;
    }

}
