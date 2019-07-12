package com.hedvig.backoffice.graphql.types;

import lombok.Value;
import com.hedvig.backoffice.services.tickets.dto.TicketType;
import com.hedvig.backoffice.services.tickets.dto.TicketStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class TicketInput {
  String assignedTo;
  float priority; //0 <= priority <= 1
  TicketType type;
  LocalDate remindNotificationDate;
  LocalTime remindNotificationTime;
  String remindMessage;
  String description;
  TicketStatus status;
}

