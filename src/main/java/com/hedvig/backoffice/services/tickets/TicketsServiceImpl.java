package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.graphql.types.TicketIn;
import com.hedvig.backoffice.services.tickets.dto.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class TicketsServiceImpl implements TicketsService {
  private final TicketServiceClient ticketServiceClient;

  public TicketsServiceImpl (TicketServiceClient ticketServiceClient) {
    this.ticketServiceClient = ticketServiceClient;
  }

  @Override
  public List<TicketDto> getAllTickets() {
    List<TicketDto> ticketDtos = this.ticketServiceClient.getTickets();
    int size = ticketDtos.size();
    return ticketDtos;
  }

  @Override
  public TicketDto getTicketById(String ticketId) {
    return ticketServiceClient.getTicket(ticketId);
  }

  @Override
  public TicketDto createNewTicket( TicketIn t ) {

    //This is where the ID is generated
    String id  = UUID.randomUUID().toString();

    TicketDto ticket = new TicketDto(
        id,
        t.getAssignedTo(),
        Instant.now(), // Back-office timestamps the ticket
        t.getCreatedBy(),
        t.getPriority(),
        t.getType(),
        t.getRemindNotificationDate(),
        t.getRemindNotificationTime(),
        t.getDescription(),
        t.getStatus()
    );

    this.ticketServiceClient.createTicket( id, ticket );
    //Obs, for now the ticket is returned directly from here...
    return ticket;
  }

  @Override
  public TicketDto changeDescription(String ticketId, String newDescription) {
    return this.ticketServiceClient.changeDescription(ticketId, newDescription);
  }

  @Override
  public TicketDto assignToTeamMember(String ticketId, String assignTo) {
    return this.ticketServiceClient.changeAssignedTo(ticketId, assignTo);
  }

  @Override
  public TicketDto changeStatus(String ticketId, TicketStatus newStatus) {
    return this.ticketServiceClient.changeStatus(ticketId, newStatus);
  }


}
