package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.graphql.types.RemindNotification;
import com.hedvig.backoffice.services.tickets.dto.*;

import java.util.List;
import java.util.UUID;

public class TicketServiceImpl implements TicketService {
  private final TicketServiceClient ticketServiceClient;

  public TicketServiceImpl(TicketServiceClient ticketServiceClient) {
    this.ticketServiceClient = ticketServiceClient;
  }

  @Override
  public List<TicketDto> getAllTickets() {
    List<TicketDto> ticketDtos = this.ticketServiceClient.getTickets();
    int size = ticketDtos.size();
    return ticketDtos;
  }

  @Override
  public TicketDto getTicketById(UUID ticketId) {
    return ticketServiceClient.getTicket(ticketId);
  }

  @Override
  public TicketDto createNewTicket(TicketDto ticket, String createdBy) {
    return this.ticketServiceClient.createTicket(ticket);
  }

  @Override
  public TicketDto changeDescription(UUID ticketId, String newDescription, String modifiedBy) {
    return this.ticketServiceClient.changeDescription(ticketId, new NewDescriptionDto( newDescription,  modifiedBy));
  }

  @Override
  public TicketDto assignToTeamMember(UUID ticketId, String assignTo, String modifiedBy) {
    return this.ticketServiceClient.changeAssignedTo(ticketId,new NewAssignedToDto(assignTo, modifiedBy));
  }

  @Override
  public TicketDto changeStatus(UUID ticketId, TicketStatus newStatus, String modifiedBy) {
    return this.ticketServiceClient.changeStatus(ticketId, new NewStatusDto(newStatus, modifiedBy));
  }

  @Override
  public TicketDto changeReminder(UUID ticketId, RemindNotification newReminder, String modifiedBy) {
    return this.ticketServiceClient.changeReminder(ticketId, new NewReminderDto( newReminder, modifiedBy));
  }

}
