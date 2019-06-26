package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.graphql.types.TicketIn;
import com.hedvig.backoffice.services.tickets.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface TicketsService {

  List<TicketDto> getAllTickets ();

  TicketDto getTicketById(String ticketId );

  TicketDto createNewTicket ( TicketIn ticket );

  TicketDto changeDescription (String ticketId, String newDescription ) ;

  TicketDto assignToTeamMember (String ticketId, String teamMemberId ) ;

  TicketDto changeStatus (String ticketId, TicketStatus newStatus);

}
