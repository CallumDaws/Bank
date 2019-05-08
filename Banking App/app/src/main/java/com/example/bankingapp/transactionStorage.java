package com.example.bankingapp;

import java.io.Serializable;
import java.util.Date;

public class transactionStorage implements Serializable {
    private double moneyIn;
    private double moneyOut;
    private String description;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;
    private int reference;
    private double amount;

  /*  @Override
    public String toString() {
        return "transactionStorage{" +
                "category" + category +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", reference=" + reference +
                '}';
    }*/

    public double calculateAmount(){
         amount = moneyIn - moneyOut;
         return amount;
    }

    public double getAmount(){ return amount;   }
    public double getMoneyIn() {
        return moneyIn;
    }

    public void setMoneyIn(double moneyIn) {
        this.moneyIn = moneyIn;
    }

    public int getReference() {
        return reference;
    }

    public void setReference(int reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMoneyOut() {
        return moneyOut;
    }

    public void setMoneyOut(double moneyOut) {
        this.moneyOut = moneyOut;
    }

}
