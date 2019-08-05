package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

@Value
public class ChangeAssignToDto {
  private String assignedTo;
  private String changedBy;
}
