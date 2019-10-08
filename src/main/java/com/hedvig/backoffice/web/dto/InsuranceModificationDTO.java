package com.hedvig.backoffice.web.dto;

import java.util.List;
import java.util.UUID;

import com.hedvig.backoffice.services.product_pricing.dto.ExtraBuildingDTO;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class InsuranceModificationDTO {

  public UUID idToBeReplaced;

  public String memberId;
  public Boolean isStudent;

  public String street;
  public String city;
  public String zipCode;
  public Integer floor;

  public Float livingSpace;
  public String houseType;
  public Integer personsInHouseHold;

  public List<SafetyIncreaserType> safetyIncreasers;

  public Integer ancillaryArea;
  public Integer yearOfConstruction;
  public Integer numberOfBathrooms;
  public List<ExtraBuildingDTO> extraBuildings;
  public Boolean isSubleted;
}
