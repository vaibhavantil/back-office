package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Value
public class TicketDto {
  private UUID id;
  private String assignedTo;
  private Instant creationDate;
  private String memberId;
  private String referenceId;
  private String createdBy;
  @Min(0)
  @Max(1)
  private float priority;
  private TicketType type;
  private LocalDate remindNotificationDate;
  private LocalTime remindNotificationTime;
  private String remindMessage;
  private String description;
  private TicketStatus status;

}
