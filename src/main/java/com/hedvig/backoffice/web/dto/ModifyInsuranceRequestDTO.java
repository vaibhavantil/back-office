package com.hedvig.backoffice.web.dto;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModifyInsuranceRequestDTO {

  public UUID insuranceIdToBeReplaced;
  public LocalDate terminationDate;
  public UUID insuranceIdToReplace;
  public LocalDate activationDate;
  public String memberId;
}
