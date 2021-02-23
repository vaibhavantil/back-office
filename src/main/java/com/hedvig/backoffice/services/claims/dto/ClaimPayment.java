package com.hedvig.backoffice.services.claims.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hedvig.backoffice.util.DoubleOrMonetaryAmountToMonetaryAmountDeserializer;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClaimPayment {
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

    public boolean exGratia;

    @NotNull
    ClaimPaymentType type;

    @NotNull
    String handlerReference;

    boolean sanctionListSkipped;

    @Nullable
    @JsonProperty("id")
    public String paymentId;

    @JsonProperty("date")
    LocalDateTime registrationDate;

    ClaimPaymentStatus payoutStatus;

    UUID transactionId;

    String carrier;

    public ClaimPayment(
        @NotNull String claimId,
        @NotNull MonetaryAmount amount,
        @NotNull MonetaryAmount deductible,
        @NotNull String note,
        boolean exGratia,
        @NotNull ClaimPaymentType type,
        @NotNull String handlerReference,
        boolean sanctionListSkipped,
        @NotNull String carrier
    ) {
        this.claimId = claimId;
        this.amount = amount;
        this.deductible = deductible;
        this.note = note;
        this.exGratia = exGratia;
        this.type = type;
        this.handlerReference = handlerReference;
        this.sanctionListSkipped = sanctionListSkipped;
        this.carrier = carrier;
    }
}
