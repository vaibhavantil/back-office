package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

@Value
public class ChangeDescriptionDto {
  private String description;
  private String changedBy;
}
