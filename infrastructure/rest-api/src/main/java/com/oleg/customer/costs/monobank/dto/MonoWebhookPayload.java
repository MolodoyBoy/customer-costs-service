package com.oleg.customer.costs.monobank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MonoWebhookPayload {
    private String type;
    private Data data;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }

    public static class Data {
        private String account;

        @JsonProperty("statementItem")
        private StatementItem statementItem;

        public String getAccount() { return account; }
        public void setAccount(String account) { this.account = account; }

        public StatementItem getStatementItem() { return statementItem; }
        public void setStatementItem(StatementItem statementItem) { this.statementItem = statementItem; }
    }
}
