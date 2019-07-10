package com.hedvig.backoffice.graphql.types;

import lombok.Value;
import com.hedvig.backoffice.services.tickets.dto.TicketType;
import com.hedvig.backoffice.services.tickets.dto.TicketStatus;


/*
  Note: TicketIn is used when creating new tickets on the front-end.
  It lacks an ID field and createdDate field. Those are set here
  in the back-office.
  [Front-end] -> TicketIn -> [Back-office] -> TicketDto -> ...[Ticket-service]
*/


@Value
public class TicketIn {
  String assignedTo;
  String createdBy;
  float priority; //0 <= priority <= 1
  TicketType type;
  RemindNotification reminder;
  String description;
  TicketStatus status;
}

