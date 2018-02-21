package com.hedvig.backoffice.services.claims.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.hedvig.backoffice.services.claims.ClaimState;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Claim extends ClaimBackOffice {

    private String audioURL;
    private ClaimState state;
    private BigDecimal reserve;
    private String type;

    private List<ClaimNote> notes;
    private List<ClaimPayment> payments;
    private List<ClaimAsset> assets;
    private List<ClaimEvent> events;
    private List<ClaimData> data;

    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;

}
