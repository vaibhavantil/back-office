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
    return this.ticketServiceClient.getTickets();
  }

  @Override
  public TicketDto getTicketById(UUID ticketId) {
    return ticketServiceClient.getTicket(ticketId);
  }

  @Override
  public void createNewTicket(CreateTicketDto ticket, String createdBy) {
    this.ticketServiceClient.createTicket(ticket);
  }

  @Override
  public void changeDescription(UUID ticketId, String newDescription, String modifiedBy) {
    this.ticketServiceClient.changeDescription(ticketId, new NewDescriptionDto( newDescription,  modifiedBy));
  }

  @Override
  public void assignToTeamMember(UUID ticketId, String assignTo, String modifiedBy) {
    this.ticketServiceClient.changeAssignedTo(ticketId,new NewAssignedToDto(assignTo, modifiedBy));
  }

  @Override
  public void changeStatus(UUID ticketId, TicketStatus newStatus, String modifiedBy) {
    this.ticketServiceClient.changeStatus(ticketId, new NewStatusDto(newStatus, modifiedBy));
  }

  @Override
  public void changeReminder(UUID ticketId, RemindNotification newReminder, String modifiedBy) {
    this.ticketServiceClient.changeReminder(ticketId, new NewReminderDto( newReminder, modifiedBy));
  }

  @Override
  public void changePriority(UUID ticketId, float newPriority, String modifiedBy) {
    this.ticketServiceClient.changePriority(ticketId, new NewPriorityDto( newPriority, modifiedBy));
  }

}
