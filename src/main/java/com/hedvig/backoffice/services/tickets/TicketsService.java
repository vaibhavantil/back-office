package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.services.tickets.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface TicketsService {

  List<TicketDto> getAllTickets ();

  TicketDto getTicketById(String ticketId );

  TicketDto createNewTicket (
      String assignedTo,
      //LocalDate creationDate,
      String createdBy,
      TicketPriority priority,
     //TicketType type,
      LocalDate  remindNotificationDate,
      LocalTime remindNotificationTime,
      String description
   // TicketStatus status,
   // List<TicketTag> tags
                        );


  void updateTicket (
       String ticketID,
       String assignedTo,
       LocalDate creationDate,
       String createdBy,
       TicketPriority priority,
       TicketType type,
       LocalDateTime remindNotificationDate,
       String description,
       TicketStatus status,
       List<TicketTag> tags );


  TicketDto changeDescription (String ticketId, String newDescription ) ;

  TicketDto assignToTeamMember (String ticketId, String teamMemberId ) ;
}
