package com.hedvig.backoffice.services.tickets;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.tickets.dto.*;
import com.hedvig.backoffice.web.dto.PersonnelDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TicketsServiceStub implements TicketsService{
  private HashMap<String, Ticket> tickets = new HashMap<String, Ticket>();
  private List<String> personnels = new ArrayList<String>();

  private List<PersonnelDTO> personnelDTOs = new ArrayList<PersonnelDTO>();
  private final PersonnelService personnelService;


  public TicketsServiceStub( PersonnelService personnelService
  ) {
    String[] teamMembers = {"Unassigned", "TeamMember2", "TeamMember3", "TeamMember4", "TeamMember5"};
    TicketPriority[] priorities = { TicketPriority.LOW, TicketPriority.MEDIUM, TicketPriority.HIGH };
    TicketType[] type = {TicketType.CALL_ME, TicketType.CLAIM, TicketType.MESSAGE, TicketType.REMIND};
    List<TicketTag> tags = new ArrayList<TicketTag>();
    tags.add( TicketTag.UNCATEGORIZED);
    tags.add( TicketTag.ETC);

    this.tickets = new HashMap<String, Ticket>();
    this.personnelService = personnelService;
    this.personnelDTOs = this.personnelService.list();

    this.personnels = this.personnelDTOs.stream().map( person -> person.getName()).collect(Collectors.toList());

    //Generate Fake Tickets
    int NUM_TICKETS = 10;
    for (int i = 0; i < NUM_TICKETS; i++  ){
      Ticket t = new Ticket (
          teamMembers[i % teamMembers.length],
        //  personnels.get( i % personnels.size()),
          LocalDate.now(),
          teamMembers[i % teamMembers.length],
        ///personnels.get( i % personnels.size()),
          priorities [i % priorities.length],
          type [i % type.length],
          null,
          "A short description of the ticket contents",
          TicketStatus.WAITING,
          tags
        );
      tickets.put(t.getId().toString(), t );
    }
  }

  @Override
  public List<Ticket> getAllTickets() {
    return tickets.values().stream().collect(Collectors.toList());
  }

  @Override
  public Ticket getTicketById(String ticketId) {
    try {
      Ticket t = tickets.get(ticketId);
      return t;
    }
    catch (Error e ) {
      return null;
      // Handle error
    }
  }

  @Override
  public void createNewTicket(
    String          assignedTo,
    LocalDate creationDate,
    String          createdBy,
    TicketPriority priority,
    TicketType type,
    LocalDate       remindNotificationDate,
    String          description,
    TicketStatus status,
    List<TicketTag> tags
  ) {
    Ticket newTicket = new Ticket( assignedTo, creationDate, createdBy, priority,
      type, remindNotificationDate, description, status, tags );

    tickets.put( newTicket.getId().toString(), newTicket );
  }

  @Override
  public void updateTicket (
    String ticketID,
    String          assignedTo,
    LocalDate creationDate,
    String          createdBy,
    TicketPriority priority,
    TicketType type,
    LocalDate       remindNotificationDate,
    String          description,
    TicketStatus status,
    List<TicketTag> tags ) {
    // Do some crazy
  }

  @Override
  public Ticket changeDescription(String ticketId, String newDescription) {
    try {
      Ticket oldTicket = tickets.get(ticketId);
      Ticket mutatedTicket = new Ticket (
        oldTicket.getId(),
        oldTicket.getAssignedTo(),
        oldTicket.getCreationDate(),
        oldTicket.getCreatedBy(),
        oldTicket.getPriority(),
        oldTicket.getType(),
        oldTicket.getRemindNotificationDate(),
        newDescription,
        oldTicket.getStatus(),
        oldTicket.getTags());
      tickets.replace( ticketId, mutatedTicket);
      return mutatedTicket;
    }
    catch (Error e) {
      return null; // No go
      //TODO Handle error
    }
  }

  @Override
  public Ticket assignToTeamMember(String ticketId, String teamMemberId) {
    try {
      Ticket oldTicket = tickets.get(ticketId);
      Ticket mutatedTicket = new Ticket (
        oldTicket.getId(),
        teamMemberId,
        oldTicket.getCreationDate(),
        oldTicket.getCreatedBy(),
        oldTicket.getPriority(),
        oldTicket.getType(),
        oldTicket.getRemindNotificationDate(),
        oldTicket.getDescription(),
        oldTicket.getStatus(),
        oldTicket.getTags());
      tickets.replace( ticketId, mutatedTicket);
      return mutatedTicket;
    }
    catch (Error e) {
      return null; // No go
      //TODO Handle error
    }
  }
}
