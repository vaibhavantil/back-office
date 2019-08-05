package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

@Value
public class ChangeStatusDto {
  private TicketStatus status;
  private String changedBy;
}
