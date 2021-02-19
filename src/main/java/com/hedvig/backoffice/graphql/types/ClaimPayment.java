package com.hedvig.backoffice.graphql.types;

import com.hedvig.backoffice.services.claims.dto.ClaimPaymentStatus;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import javax.money.MonetaryAmount;
import lombok.Value;

@Value
public class ClaimPayment {
    String id;
    MonetaryAmount amount;
    MonetaryAmount deductible;
    String note;
    ClaimPaymentType type;
    Instant timestamp;
    Boolean exGratia;
    ClaimPaymentStatus status;
    Optional<UUID> transactionId;


    public static ClaimPayment fromDto(com.hedvig.backoffice.services.claims.dto.ClaimPayment dto) {
        return new ClaimPayment(
            dto.getPaymentId(),
            dto.getAmount(),
            dto.getDeductible(),
            dto.getNote(),
            dto.getType() != null ? ClaimPaymentType.valueOf(dto.getType().toString()) : ClaimPaymentType.Manual,
            dto.getRegistrationDate().toInstant(ZoneOffset.UTC),
            dto.getExGratia(),
            dto.getPayoutStatus(),
            //FIXME: This is always null in the DB. Should we remove it?
            dto.getTransactionId() != null ? Optional.of(dto.getTransactionId()) : Optional.empty());
    }
}
