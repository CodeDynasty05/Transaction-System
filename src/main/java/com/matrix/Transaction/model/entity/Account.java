package com.matrix.Transaction.model.entity;

import jakarta.persistence.*;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {
    private String accountNumber;
    private Double balance;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account")
    @ToString.Exclude
    private List<Transaction> transactions;

    public Account(String accountNumber, Double balance, AccountStatus accountStatus, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountStatus = accountStatus;
        this.customer = customer;
    }

    public Account() {
    }

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public Double getBalance() {
        return this.balance;
    }

    public AccountStatus getAccountStatus() {
        return this.accountStatus;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Account)) return false;
        final Account other = (Account) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$accountNumber = this.getAccountNumber();
        final Object other$accountNumber = other.getAccountNumber();
        if (this$accountNumber == null ? other$accountNumber != null : !this$accountNumber.equals(other$accountNumber))
            return false;
        final Object this$balance = this.getBalance();
        final Object other$balance = other.getBalance();
        if (this$balance == null ? other$balance != null : !this$balance.equals(other$balance)) return false;
        final Object this$accountStatus = this.getAccountStatus();
        final Object other$accountStatus = other.getAccountStatus();
        if (this$accountStatus == null ? other$accountStatus != null : !this$accountStatus.equals(other$accountStatus))
            return false;
        final Object this$customer = this.getCustomer();
        final Object other$customer = other.getCustomer();
        if (this$customer == null ? other$customer != null : !this$customer.equals(other$customer)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Account;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $accountNumber = this.getAccountNumber();
        result = result * PRIME + ($accountNumber == null ? 43 : $accountNumber.hashCode());
        final Object $balance = this.getBalance();
        result = result * PRIME + ($balance == null ? 43 : $balance.hashCode());
        final Object $accountStatus = this.getAccountStatus();
        result = result * PRIME + ($accountStatus == null ? 43 : $accountStatus.hashCode());
        final Object $customer = this.getCustomer();
        result = result * PRIME + ($customer == null ? 43 : $customer.hashCode());
        return result;
    }

    public String toString() {
        return "Account(accountNumber=" + this.getAccountNumber() + ", balance=" + this.getBalance() + ", accountStatus=" + this.getAccountStatus() + ", customer=" + this.getCustomer() + ")";
    }

    public static class AccountBuilder {
        private String accountNumber;
        private Double balance;
        private AccountStatus accountStatus;
        private Customer customer;

        AccountBuilder() {
        }

        public AccountBuilder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public AccountBuilder balance(Double balance) {
            this.balance = balance;
            return this;
        }

        public AccountBuilder accountStatus(AccountStatus accountStatus) {
            this.accountStatus = accountStatus;
            return this;
        }

        public AccountBuilder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Account build() {
            return new Account(this.accountNumber, this.balance, this.accountStatus, this.customer);
        }

        public String toString() {
            return "Account.AccountBuilder(accountNumber=" + this.accountNumber + ", balance=" + this.balance + ", accountStatus=" + this.accountStatus + ", customer=" + this.customer + ")";
        }
    }
}


