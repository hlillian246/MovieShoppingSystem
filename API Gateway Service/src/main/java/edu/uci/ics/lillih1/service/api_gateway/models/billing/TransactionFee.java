package edu.uci.ics.lillih1.service.api_gateway.models.billing;

public class TransactionFee {
    private String value;
    private String currency;

    public TransactionFee() {
    }

    public TransactionFee(String value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
