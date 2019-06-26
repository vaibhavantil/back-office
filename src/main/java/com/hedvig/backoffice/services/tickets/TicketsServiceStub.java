package com.hedvig.backoffice.services.tickets;
import com.hedvig.backoffice.graphql.types.TicketIn;
import com.hedvig.backoffice.services.tickets.dto.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TicketsServiceStub implements TicketsService {

  private HashMap<String, TicketDto> tickets = new HashMap<String, TicketDto>();

  public TicketsServiceStub() {
    String[] teamMembers = {"Matilda", "Karl", "Johanna", "Tomas", "Kalle", "Emma", "Sara", "Axel", "Unassigned"};
    TicketPriority[] priorities = {TicketPriority.LOW, TicketPriority.MEDIUM, TicketPriority.HIGH};
    TicketType[] type = {TicketType.CALL_ME, TicketType.CLAIM, TicketType.MESSAGE, TicketType.REMIND};

    this.tickets = new HashMap<String, TicketDto>();

    //Generate Fake Tickets
    int NUM_TICKETS = 10;
    for (int i = 0; i < NUM_TICKETS; i++) {
      String id = UUID.randomUUID().toString();
      TicketDto t = new TicketDto(
        id,
        teamMembers[i % teamMembers.length],
        Instant.now(),
        teamMembers[i % teamMembers.length],
        priorities[i % priorities.length],
        type[i % type.length],
        LocalDate.now(),
        LocalTime.now(),
        "A short description of the ticket contents",
        TicketStatus.WAITING
      );

      tickets.put(id, t);
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
    } catch (Error e) {
      return null;
      // Handle error
    }
  }

  @Override
  public TicketDto createNewTicket(TicketIn t) {
    String id = UUID.randomUUID().toString();
    TicketDto newT = new TicketDto(
      id,
      t.getAssignedTo(),
      Instant.now(),
      t.getCreatedBy(),
      t.getPriority(),
      t.getType(),
      t.getRemindNotificationDate(),
      t.getRemindNotificationTime(),
      t.getDescription(),
      t.getStatus()
    );

    tickets.put(id, newT);

    return newT;
  }

  @Override
  public TicketDto changeDescription(String ticketId, String newDescription) {
    try {
      TicketDto t = tickets.get(ticketId);
      TicketDto updateT = new TicketDto(
        t.getId(),
        t.getAssignedTo(),
        Instant.now(),
        t.getCreatedBy(),
        t.getPriority(),
        t.getType(),
        t.getRemindNotificationDate(),
        t.getRemindNotificationTime(),
        newDescription,
        t.getStatus()
      );
      tickets.replace(updateT.getId(), updateT);
      return updateT;
    } catch (Error e) {
      return null;
    }
  }

  @Override
  public TicketDto assignToTeamMember(String ticketId, String assignTo) {
    try {
      TicketDto t = tickets.get(ticketId);
      TicketDto updateT = new TicketDto(
        t.getId(),
        assignTo,
        Instant.now(),
        t.getCreatedBy(),
        t.getPriority(),
        t.getType(),
        t.getRemindNotificationDate(),
        t.getRemindNotificationTime(),
        t.getDescription(),
        t.getStatus()
      );
      tickets.replace(updateT.getId(), updateT);
      return updateT;
    } catch (Error e) {
      return null;
    }
  }

  @Override
  public TicketDto changeStatus(String ticketId, TicketStatus newStatus) {
    try {
      TicketDto t = tickets.get(ticketId);
      TicketDto updateT = new TicketDto(
        t.getId(),
        t.getAssignedTo(),
        Instant.now(),
        t.getCreatedBy(),
        t.getPriority(),
        t.getType(),
        t.getRemindNotificationDate(),
        t.getRemindNotificationTime(),
        t.getDescription(),
        newStatus
      );
      tickets.replace(updateT.getId(), updateT);
      return updateT;
    } catch (Error e) {
      return null;
    }
  }


}
