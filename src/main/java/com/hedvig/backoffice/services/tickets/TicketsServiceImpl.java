package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.graphql.types.RemindNotification;
import com.hedvig.backoffice.graphql.types.TicketIn;
import com.hedvig.backoffice.services.tickets.dto.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
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
  public TicketDto createNewTicket( TicketIn t, String createdBy ) {

    //This is where the ID is generated
    String id  = UUID.randomUUID().toString();

    LocalDate remindDate;
    LocalTime remindTime ;
    String remindMessage;

    //A reminder is optional
    if (t.getReminder() == null ) {
      remindDate = null;
      remindTime = null;
      remindMessage = "";
    }
    else {
      remindDate = t.getReminder().getDate();
      remindTime = t.getReminder().getTime();
      remindMessage = t.getReminder().getMessage();
    }

    TicketDto ticket = new TicketDto(
        id,
        t.getAssignedTo(),
        Instant.now(), // Back-office timestamps the ticket
        createdBy,
        t.getPriority(),
        t.getType(),
        remindDate,
        remindTime,
        remindMessage,
        t.getDescription(),
        t.getStatus()
    );

    this.ticketServiceClient.createTicket( id, ticket );
    //NB! For now the ticket is returned directly from here...
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

  @Override
  public TicketDto changeReminder(String ticketId, RemindNotification newReminder) {
    return this.ticketServiceClient.changeReminder(ticketId, newReminder );
  }

}
