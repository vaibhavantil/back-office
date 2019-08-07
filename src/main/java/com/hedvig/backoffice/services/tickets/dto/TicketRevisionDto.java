package com.hedvig.backoffice.services.tickets.dto;

import com.hedvig.backoffice.services.tickets.TicketChangeType;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class TicketRevisionDto {
  String assignedTo;
  Float manualPriority;
  LocalDate remindDate;
  LocalTime remindTime;
  String remindMessage;
  TicketStatus status;
  Instant changedAt;
  TicketChangeType changeType;
  String changedBy;
  String description;
}
