package edu.uci.ics.lillih1.service.api_gateway.models.billing;

import java.util.List;

public class TransactionModel {
    private String transactionId;
    private String state;
    private AmountModel amountModel;
    private TransactionFee transaction_fee;
    private String create_time;
    private String update_time;
    private List<OrderItemModel> items;

    public TransactionModel() {
    }

    public TransactionModel(String transactionId, String state, AmountModel amountModel, TransactionFee transaction_fee, String create_time, String update_time, List<OrderItemModel> items) {
        this.transactionId = transactionId;
        this.state = state;
        this.amountModel = amountModel;
        this.transaction_fee = transaction_fee;
        this.create_time = create_time;
        this.update_time = update_time;
        this.items = items;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public AmountModel getAmountModel() {
        return amountModel;
    }

    public void setAmountModel(AmountModel amountModel) {
        this.amountModel = amountModel;
    }

    public TransactionFee getTransaction_fee() {
        return transaction_fee;
    }

    public void setTransaction_fee(TransactionFee transaction_fee) {
        this.transaction_fee = transaction_fee;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public List<OrderItemModel> getItems() {
        return items;
    }

    public void setItems(List<OrderItemModel> items) {
        this.items = items;
    }
}
