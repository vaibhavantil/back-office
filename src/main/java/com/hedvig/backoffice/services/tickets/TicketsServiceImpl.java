package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.services.tickets.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
  public TicketDto createNewTicket(
      String assignedTo,
//    LocalDate creationDate, //This is assigned here V
      String createdBy,
      TicketPriority priority,
  //  TicketType type,
      LocalDate remindNotificationDate,
      LocalTime remindNotificationTime,
      String description
    //TicketStatus status,
  //List<TicketTag> tags
                              ) {
    ArrayList<TicketTag> tags = new ArrayList<TicketTag>();
    String id  = UUID.randomUUID().toString();
    TicketDto ticket = new TicketDto(
                                      id,
                                      assignedTo,
                                      LocalDate.now(),
                                      createdBy,
                                      priority,
                                      TicketType.REMIND,
                                      remindNotificationDate,
                                      remindNotificationTime,
                                      description,
                                      TicketStatus.WAITING,
                                      tags       );

    this.ticketServiceClient.createTicket( id, ticket );
    return ticket;
  }

  @Override
  public void updateTicket(
        String ticketID, String assignedTo,
        LocalDate creationDate,
        String createdBy,
        TicketPriority priority,
        TicketType type,
        LocalDateTime remindNotificationDate,
        String description,
        TicketStatus status,
        List<TicketTag> tags) {

  }

  @Override
  public TicketDto changeDescription(String ticketId, String newDescription) {
    return this.ticketServiceClient.changeDescription(ticketId, newDescription);
  }

  @Override
  public TicketDto assignToTeamMember(String ticketId, String assignTo) {
    return this.ticketServiceClient.changeAssignedTo(ticketId, assignTo);
  }
}
