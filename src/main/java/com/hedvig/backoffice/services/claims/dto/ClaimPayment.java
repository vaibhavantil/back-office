package com.hedvig.backoffice.services.claims.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClaimPayment {

    public ClaimPayment(
        @NotNull String claimId,
        @NotNull MonetaryAmount amount,
        @NotNull MonetaryAmount deductible,
        @NotNull String note,
        @NotNull boolean exGratia,
        @NotNull ClaimPaymentType type,
        @NotNull String handlerReference,
        @NotNull boolean sanctionListSkipped) {
        this.claimId = claimId;
        this.amount = amount;
        this.deductible = deductible;
        this.note = note;
        this.exGratia = exGratia;
        this.type = type;
        this.handlerReference = handlerReference;
        this.sanctionListSkipped = sanctionListSkipped;
    }

    @NotNull
    public String claimId;

    @NotNull
    public MonetaryAmount amount;

    @NotNull
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
