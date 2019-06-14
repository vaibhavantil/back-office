package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Value
public class Ticket {
  UUID id;
  String assignedTo;
  LocalDate creationDate ;
  String createdBy;
  TicketPriority priority;
  TicketType type;
  LocalDate remindNotificationDate;
  String description;
  TicketStatus status;
  List<TicketTag> tags;

  public Ticket(
                String          assignedTo,
                LocalDate       creationDate,
                String          createdBy,
                TicketPriority  priority,
                TicketType      type,
                LocalDate       remindNotificationDate,
                String          description,
                TicketStatus    status,
                List<TicketTag> tags                     ) {
    this.id = UUID.randomUUID(); //Bad way to generate ID?
    this.assignedTo = assignedTo;
    this.creationDate = creationDate;
    this.createdBy = createdBy;
    this.priority = priority;
    this.type = type;
    this.remindNotificationDate = remindNotificationDate;
    this.description = description;
    this.status = status;
    this.tags = tags;
  }

  //Used for when mutating a ticket:
  public Ticket(
    UUID id,
    String          assignedTo,
    LocalDate       creationDate,
    String          createdBy,
    TicketPriority  priority,
    TicketType      type,
    LocalDate       remindNotificationDate,
    String          description,
    TicketStatus    status,
    List<TicketTag> tags                     ) {
    this.id = id;
    this.assignedTo = assignedTo;
    this.creationDate = creationDate;
    this.createdBy = createdBy;
    this.priority = priority;
    this.type = type;
    this.remindNotificationDate = remindNotificationDate;
    this.description = description;
    this.status = status;
    this.tags = tags;
  }




}
