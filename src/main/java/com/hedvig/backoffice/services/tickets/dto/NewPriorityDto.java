package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Value
public class NewPriorityDto {
  @Min(0)
  @Max(1)
  private float priority;
  private String modifiedBy;
}
