package com.hedvig.backoffice.services.tickets.dto;

import com.hedvig.backoffice.graphql.types.RemindNotification;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class TicketDto {
  String id;
  String assignedTo;
  Instant creationDate;
  String createdBy;
  float priority; //0 <= priority <= 1
  TicketType type;
  LocalDate remindNotificationDate;
  LocalTime remindNotificationTime;
  String remindMessage;
  String description;
  TicketStatus status;

}
