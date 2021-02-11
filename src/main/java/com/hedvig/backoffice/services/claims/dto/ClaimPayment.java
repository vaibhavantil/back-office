package com.hedvig.backoffice.services.claims.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hedvig.backoffice.util.DoubleOrMonetaryAmountToMonetaryAmountDeserializer;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimPayment {

    public ClaimPayment(
        @NotNull String claimId,
        @NotNull MonetaryAmount amount,
        @NotNull MonetaryAmount deductible,
        @NotNull String note,
        boolean exGratia,
        @NotNull ClaimPaymentType type,
        @NotNull String handlerReference,
        boolean sanctionListSkipped) {
        this.claimId = claimId;
        this.amount = amount;
        this.deductible = deductible;
        this.note = note;
        this.exGratia = exGratia;
        this.type = type;
        this.handlerReference = handlerReference;
        this.sanctionListSkipped = sanctionListSkipped;
    }

    public ClaimPayment(
        @NotNull String claimId,
        @NotNull Double amount,
        @NotNull Double deductible,
        @NotNull String note,
        boolean exGratia,
        @NotNull ClaimPaymentType type,
        @NotNull String handlerReference,
        boolean sanctionListSkipped,
        String id,
        LocalDateTime date,
        ClaimPaymentStatus payoutStatus,
        UUID transactionId) {
        this.claimId = claimId;
        this.amount = Money.of(amount, "SEK");
        this.deductible = Money.of(deductible, "SEK");
        this.note = note;
        this.exGratia = exGratia;
        this.type = type;
        this.handlerReference = handlerReference;
        this.sanctionListSkipped = sanctionListSkipped;
        this.paymentId = id;
        this.registrationDate = date;
        this.payoutStatus = payoutStatus;
        this.transactionId = transactionId;

    }

    @NotNull
    public String claimId;

    @NotNull
    @JsonDeserialize(using = DoubleOrMonetaryAmountToMonetaryAmountDeserializer.class)
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    public MonetaryAmount amount;

    @NotNull
    @JsonDeserialize(using = DoubleOrMonetaryAmountToMonetaryAmountDeserializer.class)
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    public MonetaryAmount deductible;

    @NotNull
    public String note;

    @NotNull
    public boolean exGratia;

    @NotNull
    ClaimPaymentType type;

    @NotNull
    String handlerReference;

    @NotNull
    boolean sanctionListSkipped;

    //The following fields are used when getting a claim, and are null when creating a payment:
    @JsonProperty("id")
    public String paymentId;

    @JsonProperty("date")
    LocalDateTime registrationDate;

    ClaimPaymentStatus payoutStatus;

    UUID transactionId;
}
