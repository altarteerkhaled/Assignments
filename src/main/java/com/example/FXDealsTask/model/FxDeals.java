package com.example.FXDealsTask.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "fx_deals")
public class FxDeals {

    @Id
    @Column(name = "deal_unique_id")
    private String dealUniqueId;

    @NotNull(message = "From currency cannot be null")
    @Pattern(regexp = "^[A-Z]{3}$", message = "From currency must be a three-letter uppercase code")
    @Column(name = "from_currency", nullable = false, length = 3)
    private String fromCurrency;

    @NotNull(message = "To currency cannot be null")
    @Pattern(regexp = "^[A-Z]{3}$", message = "To currency must be a three-letter uppercase code")
    @Column(name = "to_currency", nullable = false, length = 3)
    private String toCurrency;

    @NotNull(message = "Timestamp cannot be null")
    @Column(name = "deal_timestamp", nullable = false)
    private Timestamp dealTimestamp;

    @NotNull(message = "Amount cannot be null")
    @Column(name = "deal_amount", nullable = false)
    private BigDecimal dealAmount;

    public FxDeals(String dealUniqueId, String fromCurrency, String toCurrency, Timestamp dealTimestamp, BigDecimal dealAmount) {
        this.dealUniqueId = dealUniqueId;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.dealTimestamp = dealTimestamp;
        this.dealAmount = dealAmount;
    }

    public FxDeals() {}

    @Override
    public String toString() {
        return "FxDeals{" +
                "dealUniqueId='" + dealUniqueId + '\'' +
                ", fromCurrency='" + fromCurrency + '\'' +
                ", toCurrency='" + toCurrency + '\'' +
                ", dealTimestamp=" + dealTimestamp +
                ", dealAmount=" + dealAmount +
                '}';
    }
}
