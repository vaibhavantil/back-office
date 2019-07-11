package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.graphql.types.RemindNotification;
import com.hedvig.backoffice.graphql.types.TicketIn;
import com.hedvig.backoffice.services.tickets.dto.TicketDto;
import com.hedvig.backoffice.services.tickets.dto.TicketStatus;

import java.util.List;

public interface TicketService {

  List<TicketDto> getAllTickets();

  TicketDto getTicketById(String ticketId);

  TicketDto createNewTicket(TicketIn ticket, String createdBy);

  TicketDto changeDescription(String ticketId, String newDescription);

  TicketDto assignToTeamMember(String ticketId, String teamMemberId);

  TicketDto changeStatus(String ticketId, TicketStatus newStatus);

  TicketDto changeReminder(String ticketId, RemindNotification newReminder);

}
