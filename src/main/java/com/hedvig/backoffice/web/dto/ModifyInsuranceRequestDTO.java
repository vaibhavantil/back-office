package com.hedvig.backoffice.web.dto;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModifyInsuranceRequestDTO {

  public UUID insuranceIdToBeReplaced;
  public ZonedDateTime terminationDate;
  public UUID insuranceIdToReplace;
  public ZonedDateTime activationDate;
  public String memberId;

}
