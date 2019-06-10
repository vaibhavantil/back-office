package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.services.tickets.dto.Ticket;

public interface TicketsService {

  Ticket getTicketById(String ticketId );
  void createNewTicket ();

}
