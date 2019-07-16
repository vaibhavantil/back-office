package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

@Value
public class NewStatusDto {
  private TicketStatus status;
  private String modifiedBy;
}
