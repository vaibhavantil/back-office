package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

@Value
public class NewAssignedToDto {
  private String assignedTo;
  private String modifiedBy;
}
