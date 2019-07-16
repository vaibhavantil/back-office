package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Value
public class TicketDto {
  private UUID id;
  private String assignedTo;
  private Instant creationDate;
  private String createdBy;
  @javax.validation.constraints.Min(0)
  @javax.validation.constraints.Max(1)
  @javax.validation.Valid
  private float priority;
  private TicketType type;
  private LocalDate remindNotificationDate;
  private LocalTime remindNotificationTime;
  private String remindMessage;
  private String description;
  private TicketStatus status;

}
