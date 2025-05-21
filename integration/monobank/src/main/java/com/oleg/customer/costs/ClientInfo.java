package com.oleg.customer.costs;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.time.Instant;

/**
 * DTO для ответа от Monobank: информация о клиенте (/personal/client-info).
 */
public class ClientInfo {

    /**
     * Внутренний идентификатор клиента.
     */
    @JsonProperty("clientId")
    private String clientId;

    /**
     * Имя клиента.
     */
    private String name;

    /**
     * URL вебхука, установленного для клиента.
     */
    private String webHookUrl;

    /**
     * Права доступа клиента (код).
     */
    private String permissions;

    /**
     * Список счетов клиента.
     */
    private List<Account> accounts;

    /**
     * Список "банок" (накопительных целей).
     */
    private List<Jar> jars;

    // ===== Getters & Setters =====

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebHookUrl() {
        return webHookUrl;
    }

    public void setWebHookUrl(String webHookUrl) {
        this.webHookUrl = webHookUrl;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Jar> getJars() {
        return jars;
    }

    public void setJars(List<Jar> jars) {
        this.jars = jars;
    }

    /**
     * Счёт пользователя.
     */
    public static class Account {
        private String id;
        private String sendId;
        private long balance;
        private long creditLimit;
        private String type;
        private int currencyCode;
        private String cashbackType;
        private List<String> maskedPan;
        private String iban;

        // ===== Getters & Setters =====

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSendId() {
            return sendId;
        }

        public void setSendId(String sendId) {
            this.sendId = sendId;
        }

        public long getBalance() {
            return balance;
        }

        public void setBalance(long balance) {
            this.balance = balance;
        }

        public long getCreditLimit() {
            return creditLimit;
        }

        public void setCreditLimit(long creditLimit) {
            this.creditLimit = creditLimit;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(int currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getCashbackType() {
            return cashbackType;
        }

        public void setCashbackType(String cashbackType) {
            this.cashbackType = cashbackType;
        }

        public List<String> getMaskedPan() {
            return maskedPan;
        }

        public void setMaskedPan(List<String> maskedPan) {
            this.maskedPan = maskedPan;
        }

        public String getIban() {
            return iban;
        }

        public void setIban(String iban) {
            this.iban = iban;
        }
    }

    /**
     * Накопительная "банка" (jar) клиента.
     */
    public static class Jar {
        private String id;
        private String sendId;
        private String title;
        private String description;
        private int currencyCode;
        private long balance;
        private long goal;

        // ===== Getters & Setters =====

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSendId() {
            return sendId;
        }

        public void setSendId(String sendId) {
            this.sendId = sendId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(int currencyCode) {
            this.currencyCode = currencyCode;
        }

        public long getBalance() {
            return balance;
        }

        public void setBalance(long balance) {
            this.balance = balance;
        }

        public long getGoal() {
            return goal;
        }

        public void setGoal(long goal) {
            this.goal = goal;
        }
    }
}