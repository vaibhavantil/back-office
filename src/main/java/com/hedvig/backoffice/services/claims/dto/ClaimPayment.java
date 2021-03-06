package com.hedvig.backoffice.services.claims.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hedvig.backoffice.services.payments.dto.SelectedPayoutDetails;
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
    public MonetaryAmount amount;

    @NotNull
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

    SelectedPayoutDetails payoutDetails;

    public ClaimPayment(
        @NotNull String claimId,
        @NotNull MonetaryAmount amount,
        @NotNull MonetaryAmount deductible,
        @NotNull String note,
        boolean exGratia,
        @NotNull ClaimPaymentType type,
        @NotNull String handlerReference,
        boolean sanctionListSkipped,
        @NotNull String carrier,
        SelectedPayoutDetails payoutDetails
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
        this.payoutDetails = payoutDetails;
    }
}
