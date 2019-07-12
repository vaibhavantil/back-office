package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.graphql.types.RemindNotification;
import com.hedvig.backoffice.services.tickets.dto.TicketDto;
import com.hedvig.backoffice.services.tickets.dto.TicketStatus;

import java.util.List;
import java.util.UUID;

public interface TicketService {

  List<TicketDto> getAllTickets();

  TicketDto getTicketById(UUID ticketId);

  TicketDto createNewTicket(TicketDto ticket, String createdBy);

  TicketDto changeDescription(UUID ticketId, String newDescription, String modifiedBy);

  TicketDto assignToTeamMember(UUID ticketId, String teamMemberId, String modifiedBy);

  TicketDto changeStatus(UUID ticketId, TicketStatus newStatus, String modifiedBy);

  TicketDto changeReminder(UUID ticketId, RemindNotification newReminder, String modifiedBy);

}
