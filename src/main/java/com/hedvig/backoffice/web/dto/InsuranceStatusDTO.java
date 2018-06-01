package com.hedvig.backoffice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class InsuranceStatusDTO {

    private String productId;

    private String memberId;

    private String memberFirstName;

    private String memberLastName;

    private List<String> safetyIncreasers;

    private String insuranceStatus;

    private String insuranceState;

    private int personsInHouseHold;

    private BigDecimal currentTotalPrice;

    private BigDecimal newTotalPrice;

    private Boolean insuredAtOtherCompany;

    private String insuranceType;

    private LocalDateTime insuranceActiveFrom;

    private LocalDateTime insuranceActiveTo;

    private boolean certificateUploaded;

    private boolean cancellationEmailSent;

    private Instant signedOn;

    private String certificateUrl;
}
