package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class TicketDto {
  String id;
  //UUID id; maybe?
  String assignedTo;
  Instant creationDate;
  String createdBy;
  TicketPriority priority;
  TicketType type;
  LocalDate remindNotificationDate;
  LocalTime remindNotificationTime;
  String description;
  TicketStatus status;

}
