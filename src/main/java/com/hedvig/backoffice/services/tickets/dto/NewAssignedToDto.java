package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

@Value
public class NewAssignedToDto {
  String assignedTo;
  String modifiedBy;
}
