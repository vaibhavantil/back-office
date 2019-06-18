package com.hedvig.backoffice.services.tickets.dto;

import lombok.Value;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Value
public class TicketDto {
  String id;
  //UUID id;
  String assignedTo;
  LocalDate creationDate ;
  String createdBy;
  TicketPriority priority;
  TicketType type;
  LocalDate remindNotificationDate;
  String description;
  TicketStatus status;
  List<TicketTag> tags;

 /* public TicketDto(
                String          assignedTo,
                LocalDate       creationDate,
                String          createdBy,
                TicketPriority  priority,
                TicketType      type,
                LocalDate       remindNotificationDate,
                String          description,
                TicketStatus    status,
                List<TicketTag> tags                     ) {
    this.id = UUID.randomUUID().toString(); //Bad way to generate ID?
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
  public TicketDto(
    String id,
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
*/
  /*
  //TODO: Just for dev. remove this later
  public TicketDto ( String id, String assignedTo ) {
    this.id = id;
    this.assignedTo = assignedTo;
    this.creationDate = LocalDate.now();
    this.createdBy = "The King";
    this.priority = TicketPriority.MEDIUM;
    this.type = TicketType.REMIND;
    this.remindNotificationDate = LocalDate.now();
    this.description = "From the SQL-database";
    this.status = TicketStatus.WAITING;
    this.tags = null;
  }
*/

}
