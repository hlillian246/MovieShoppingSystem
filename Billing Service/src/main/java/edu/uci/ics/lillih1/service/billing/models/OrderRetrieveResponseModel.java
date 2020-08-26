package edu.uci.ics.lillih1.service.billing.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRetrieveResponseModel {
    private int resultCode;
    private String message;
    private List<TransactionModel> transactions;


    public OrderRetrieveResponseModel() {
    }

    public OrderRetrieveResponseModel(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public OrderRetrieveResponseModel(int resultCode, String message, List<TransactionModel> transactions) {
        this.resultCode = resultCode;
        this.message = message;
        this.transactions = transactions;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TransactionModel> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionModel> transactions) {
        this.transactions = transactions;
    }
}