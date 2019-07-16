package com.hedvig.backoffice.graphql.types;

import lombok.Value;
import com.hedvig.backoffice.services.tickets.dto.TicketType;
import com.hedvig.backoffice.services.tickets.dto.TicketStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class TicketInput {
  private String assignedTo;
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

