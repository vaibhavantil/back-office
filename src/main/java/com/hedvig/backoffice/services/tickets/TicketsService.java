package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.services.tickets.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface TicketsService {

  List<TicketDto> getAllTickets ();

  TicketDto getTicketById(String ticketId );
  void createNewTicket (  String          assignedTo,
                          LocalDate creationDate,
                          String          createdBy,
                          TicketPriority priority,
                          TicketType type,
                          LocalDate       remindNotificationDate,
                          String          description,
                          TicketStatus status,
                          List<TicketTag> tags);

  void updateTicket (
                       String ticketID,
                       String          assignedTo,
                       LocalDate creationDate,
                       String          createdBy,
                       TicketPriority priority,
                       TicketType type,
                       LocalDate       remindNotificationDate,
                       String          description,
                       TicketStatus status,
                       List<TicketTag> tags );


  TicketDto changeDescription (String ticketId, String newDescription ) ;
  TicketDto assignToTeamMember (String ticketId, String teamMemberId ) ;
}
