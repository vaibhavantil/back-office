package com.hedvig.backoffice.services.product_pricing.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import com.hedvig.backoffice.services.product_pricing.dto.contract.ExtraBuilding;
import com.hedvig.backoffice.web.dto.ProductState;
import com.hedvig.backoffice.web.dto.SafetyIncreaserType;
import com.hedvig.backoffice.web.dto.TraceInfoDTO;
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

  private ProductState insuranceState;

  private Integer personsInHouseHold;

  private BigDecimal currentTotalPrice;

  private BigDecimal newTotalPrice;

  private Boolean insuredAtOtherCompany;

  private String currentInsurer;

  private String insuranceType;

  private LocalDateTime insuranceActiveFrom;

  private LocalDateTime insuranceActiveTo;

  private boolean certificateUploaded;

  private String certificateUrl;

  private boolean cancellationEmailSent;

  private Instant signedOn;

  private List<TraceInfoDTO> traceProduct;

  private Integer ancillaryArea;

  private Integer yearOfConstruction;

  private Integer numberOfBathrooms;

  private List<ExtraBuilding> extraBuildings;

  private  Boolean isSubleted;
}
