package com.hedvig.backoffice.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InsuranceStatusDTO {

  private String productId;

  private String memberId;

  private String memberFirstName;

  private String memberLastName;

  private String street;

  private String city;

  private String zipCode;

  private Integer floor;

  private Float livingSpace;

  private List<SafetyIncreaserType> safetyIncreasers;

  private String insuranceStatus;

  private String insuranceState;

  private Integer personsInHouseHold;

  private BigDecimal currentTotalPrice;

  private BigDecimal newTotalPrice;

  private Boolean insuredAtOtherCompany;

  private String insuranceType;

  private LocalDateTime insuranceActiveFrom;

  private LocalDateTime insuranceActiveTo;

  private boolean certificateUploaded;

  private String certificateUrl;

  private boolean cancellationEmailSent;

  private Instant signedOn;
}
