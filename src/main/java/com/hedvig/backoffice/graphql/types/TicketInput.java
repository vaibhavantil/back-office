package com.hedvig.backoffice.graphql.types;

import lombok.Value;
import com.hedvig.backoffice.services.tickets.dto.TicketType;
import com.hedvig.backoffice.services.tickets.dto.TicketStatus;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class TicketInput {
  private String assignedTo;
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

