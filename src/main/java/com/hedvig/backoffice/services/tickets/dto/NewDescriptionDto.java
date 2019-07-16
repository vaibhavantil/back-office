package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

@Value
public class NewDescriptionDto {
  private String description;
  private String modifiedBy;
}
