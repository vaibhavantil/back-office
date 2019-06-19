package com.hedvig.backoffice.services.tickets;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.tickets.dto.*;
import com.hedvig.backoffice.web.dto.PersonnelDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TicketsServiceStub implements TicketsService{

  @Override
  public List<TicketDto> getAllTickets() {
    return null;
  }

  @Override
  public TicketDto getTicketById(String ticketId) {
    return null;
  }

  @Override
  public TicketDto createNewTicket(String assignedTo, String createdBy, TicketPriority priority, LocalDate remindNotificationDate, String description) {
    return null;
  }

  @Override
  public void updateTicket(String ticketID, String assignedTo, LocalDate creationDate, String createdBy, TicketPriority priority, TicketType type, LocalDate remindNotificationDate, String description, TicketStatus status, List<TicketTag> tags) {

  }

  @Override
  public TicketDto changeDescription(String ticketId, String newDescription) {
    return null;
  }

  @Override
  public TicketDto assignToTeamMember(String ticketId, String teamMemberId) {
    return null;
  }
}
 /* private HashMap<String, TicketDto> tickets = new HashMap<String, TicketDto>();
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

    this.tickets = new HashMap<String, TicketDto>();
    this.personnelService = personnelService;
    this.personnelDTOs = this.personnelService.list();

    this.personnels = this.personnelDTOs.stream().map( person -> person.getName()).collect(Collectors.toList());

    //Generate Fake Tickets
    int NUM_TICKETS = 10;
    for (int i = 0; i < NUM_TICKETS; i++  ){
      TicketDto t = new TicketDto(
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
  public List<TicketDto> getAllTickets() {
    return tickets.values().stream().collect(Collectors.toList());
  }

  @Override
  public TicketDto getTicketById(String ticketId) {
    try {
      TicketDto t = tickets.get(ticketId);
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
    TicketDto newTicketDto = new TicketDto( assignedTo, creationDate, createdBy, priority,
      type, remindNotificationDate, description, status, tags );

    tickets.put( newTicketDto.getId().toString(), newTicketDto);
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
  public TicketDto changeDescription(String ticketId, String newDescription) {
    try {
      TicketDto oldTicketDto = tickets.get(ticketId);
      TicketDto mutatedTicketDto = new TicketDto(
        oldTicketDto.getId(),
        oldTicketDto.getAssignedTo(),
        oldTicketDto.getCreationDate(),
        oldTicketDto.getCreatedBy(),
        oldTicketDto.getPriority(),
        oldTicketDto.getType(),
        oldTicketDto.getRemindNotificationDate(),
        newDescription,
        oldTicketDto.getStatus(),
        oldTicketDto.getTags());
      tickets.replace( ticketId, mutatedTicketDto);
      return mutatedTicketDto;
    }
    catch (Error e) {
      return null; // No go
      //TODO Handle error
    }
  }

  @Override
  public TicketDto assignToTeamMember(String ticketId, String teamMemberId) {
    try {
      TicketDto oldTicketDto = tickets.get(ticketId);
      TicketDto mutatedTicketDto = new TicketDto(
        oldTicketDto.getId(),
        teamMemberId,
        oldTicketDto.getCreationDate(),
        oldTicketDto.getCreatedBy(),
        oldTicketDto.getPriority(),
        oldTicketDto.getType(),
        oldTicketDto.getRemindNotificationDate(),
        oldTicketDto.getDescription(),
        oldTicketDto.getStatus(),
        oldTicketDto.getTags());
      tickets.replace( ticketId, mutatedTicketDto);
      return mutatedTicketDto;
    }
    catch (Error e) {
      return null; // No go
      //TODO Handle error
    }
  }
}*/
