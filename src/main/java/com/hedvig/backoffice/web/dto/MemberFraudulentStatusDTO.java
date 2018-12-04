package com.hedvig.backoffice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class MemberFraudulentStatusDTO {

  private String fraudulentStatus;
  private String fraudulentStatusDescription;

}
