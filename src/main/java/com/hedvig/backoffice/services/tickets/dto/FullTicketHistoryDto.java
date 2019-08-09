package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Value
public class FullTicketHistoryDto {
  UUID id;
  Instant createdAt;
  String createdBy;
  TicketType type;
  List<TicketRevisionDto> revisions;
}
