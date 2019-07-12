package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

@Value
public class NewStatusDto {
  TicketStatus status;
  String modifiedBy;
}
