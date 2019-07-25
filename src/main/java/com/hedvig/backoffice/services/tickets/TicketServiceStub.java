package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.graphql.types.RemindNotification;
import com.hedvig.backoffice.services.tickets.dto.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class TicketServiceStub implements TicketService {

  private HashMap<UUID, TicketDto> tickets;

  public TicketServiceStub() {
    String[] teamMembers = {
      "matilda@hedvig.com",
      "karl@hedvig.com",
      "johanna@hedvig.com",
      "tomas@hedvig.com",
      "kalle@hedvig.com",
      "emma@hedvig.com",
      "sara@hedvig.com",
      "axel@hedvig.com",
      "Unassigned"
    };
    TicketType[] type = {TicketType.CALL_ME, TicketType.CLAIM, TicketType.MESSAGE, TicketType.REMIND};

    this.tickets = new HashMap<UUID, TicketDto>();
    Random rand = new Random();

    int NUM_TICKETS = 10;
    for (int i = 0; i < NUM_TICKETS; i++) {
      UUID id = UUID.randomUUID();
      TicketDto t = new TicketDto(
        id,
        teamMembers[i % teamMembers.length],
        Instant.now(),
        teamMembers[i % teamMembers.length],
        rand.nextFloat(),
        type[i % type.length],
        LocalDate.now(),
        LocalTime.now(),
        "A message to remind you...",
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
  public TicketDto getTicketById(UUID ticketId) {
    try {
      TicketDto t = tickets.get(ticketId);
      return t;
    } catch (Error e) {
      return null;
    }
  }

  @Override
  public TicketDto createNewTicket(CreateTicketDto t, String createdBy) {
    UUID id = UUID.randomUUID();
    TicketDto newT = new TicketDto(
      id,
      t.getAssignedTo(),
      Instant.now(),
      createdBy,
      t.getPriority(),
      t.getType(),
      t.getRemindNotificationDate(),
      t.getRemindNotificationTime(),
      t.getRemindMessage(),
      t.getDescription(),
      t.getStatus()
    );

    tickets.put(id, newT);

    return newT;
  }

  @Override
  public TicketDto changeDescription(UUID ticketId, String newDescription, String modifiedBy) {
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
        t.getRemindMessage(),
        newDescription,
        t.getStatus()
      );
      tickets.replace(updateT.getId(), updateT);
      return updateT;
    } catch (Error e) {
      log.error("Could not change description, error:\n" + e.toString());
      return null;
    }
  }

  @Override
  public TicketDto assignToTeamMember(UUID ticketId, String assignTo, String modifiedBy) {
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
        t.getRemindMessage(),
        t.getDescription(),
        t.getStatus()
      );
      tickets.replace(updateT.getId(), updateT);
      return updateT;
    } catch (Error e) {
      log.error("Could not assign to Team Member, error:\n" + e.toString());
      return null;
    }
  }

  @Override
  public TicketDto changeStatus(UUID ticketId, TicketStatus newStatus, String modifiedBy) {
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
        t.getRemindMessage(),
        t.getDescription(),
        newStatus
      );
      tickets.replace(updateT.getId(), updateT);
      return updateT;
    } catch (Error e) {
      log.error("Could not change status, error:\n" + e.toString());
      return null;
    }
  }

  @Override
  public TicketDto changeReminder(UUID ticketId, RemindNotification newReminder, String modifiedBy) {
    try {
      TicketDto t = tickets.get(ticketId);
      TicketDto updateT = new TicketDto(
        t.getId(),
        t.getAssignedTo(),
        Instant.now(),
        t.getCreatedBy(),
        t.getPriority(),
        t.getType(),
        newReminder.getDate(),
        newReminder.getTime(),
        newReminder.getMessage(),
        t.getDescription(),
        t.getStatus()
      );
      tickets.replace(updateT.getId(), updateT);
      return updateT;
    } catch (Error e) {
      log.error("Could not change reminder, error:\n" + e.toString());
      return null;
    }
  }

}
