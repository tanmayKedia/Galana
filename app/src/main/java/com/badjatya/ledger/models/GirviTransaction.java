package com.badjatya.ledger.models;

import java.util.UUID;

/**
 * Created by Tanmay on 9/27/2016.
 */
public class GirviTransaction
{
    String id;
    String name;
    String fatherName;
    String village;
    String Caste;
    String phoneNumber;
    long date;
    String itemName;
    int metalType;//0 for gold 1 for silver
    int interestPercentage; //0-->1.5% 1-->1.75% 2-->2.0%
    double weight;
    double tunch;
    double metalPrice;
    double calcValue;
    double moneyGiven;
    int settled; //1 means open //2 means settled //3 means gala dia

    public GirviTransaction(String id, String name, String fatherName, String village, String caste, String phoneNumber, long date, String itemName, int metalType, int interestPercentage, double weight, double tunch, Double metalPrice, Double calcValue, Double moneyGiven, int settled) {
        this.id = id;
        this.name = name;
        this.fatherName = fatherName;
        this.village = village;
        Caste = caste;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.itemName = itemName;
        this.metalType = metalType;
        this.interestPercentage = interestPercentage;
        this.weight = weight;
        this.tunch = tunch;
        this.metalPrice = metalPrice;
        this.calcValue = calcValue;
        this.moneyGiven = moneyGiven;
        this.settled = settled;
    }

    public GirviTransaction(String id) {
        this.id = id;
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

    public int getMetalType() {
        return metalType;
    }

    public void setMetalType(int metalType) {
        this.metalType = metalType;
    }

    public int getInterestPercentage() {
        return interestPercentage;
    }

    public void setInterestPercentage(int interestPercentage) {
        this.interestPercentage = interestPercentage;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getTunch() {
        return tunch;
    }

    public void setTunch(double tunch) {
        this.tunch = tunch;
    }

    public double getMetalPrice() {
        return metalPrice;
    }

    public void setMetalPrice(double metalPrice) {
        this.metalPrice = metalPrice;
    }

    public double getCalcValue() {
        return calcValue;
    }

    public void setCalcValue(double calcValue) {
        this.calcValue = calcValue;
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

    @Override
    public String toString() {
        return "GirviTransaction{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", village='" + village + '\'' +
                ", Caste='" + Caste + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", date=" + date +
                ", itemName='" + itemName + '\'' +
                ", metalType=" + metalType +
                ", interestPercentage=" + interestPercentage +
                ", weight=" + weight +
                ", tunch=" + tunch +
                ", metalPrice=" + metalPrice +
                ", calcValue=" + calcValue +
                ", moneyGiven=" + moneyGiven +
                ", settled=" + settled +
                '}';
    }
}
