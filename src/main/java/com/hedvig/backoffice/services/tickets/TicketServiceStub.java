package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.graphql.types.RemindNotification;
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;
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

    //@TODO(Change this so that we actually get tickets with memberId and referenceID when appropriate)
    for (int i = 0; i < NUM_TICKETS; i++) {
      UUID id = UUID.randomUUID();
      TicketDto ticket = new TicketDto(
        id,
        teamMembers[i % teamMembers.length],
        Instant.now(),
        "",//memberID
        "",//referenceID
        teamMembers[i % teamMembers.length],
        rand.nextFloat(),
        type[i % type.length],
        LocalDate.now(),
        LocalTime.now(),
        "A message to remind you...",
        "A short description of the ticket contents",
        TicketStatus.WAITING
      );
      tickets.put(id, ticket);
    }
  }


  @Override
  public List<TicketDto> getAllTickets(Boolean onlyResolvedTickets) {
    if (onlyResolvedTickets == null) {
      return tickets.values().stream().collect(Collectors.toList());
    } else if (onlyResolvedTickets) {
      return tickets.values().stream()
        .filter(ticketDto -> ticketDto.getStatus() == TicketStatus.RESOLVED)
        .collect(Collectors.toList());
    }

    return tickets.values().stream()
      .filter(ticketDto -> ticketDto.getStatus() != TicketStatus.RESOLVED)
      .collect(Collectors.toList());
  }

  @Override
  public TicketDto getTicketById(UUID ticketId) {
    try {
      return tickets.get(ticketId);
    } catch (Error e) {
      return null;
    }
  }

  @Override
  public UUID createNewTicket(CreateTicketDto ticketIn, String createdBy) {
    UUID id = UUID.randomUUID();
    TicketDto newTicket = new TicketDto(
      id,
      ticketIn.getAssignedTo(),
      Instant.now(),
      "",//memberID
      "",//referenceID
      createdBy,
      ticketIn.getPriority(),
      ticketIn.getType(),
      ticketIn.getRemindNotificationDate(),
      ticketIn.getRemindNotificationTime(),
      ticketIn.getRemindMessage(),
      ticketIn.getDescription(),
      ticketIn.getStatus()
    );
    tickets.put(id, newTicket);
    return id;
  }

  @Override
  public void changeDescription(UUID ticketId, String newDescription, String modifiedBy) {
    TicketDto t = tickets.get(ticketId);
    TicketDto updatedTicket = new TicketDto(
      t.getId(),
      t.getAssignedTo(),
      Instant.now(),
      "",//memberID
      "",//referenceID
      t.getCreatedBy(),
      t.getPriority(),
      t.getType(),
      t.getRemindNotificationDate(),
      t.getRemindNotificationTime(),
      t.getRemindMessage(),
      newDescription,
      t.getStatus()
    );
    if (tickets.replace(updatedTicket.getId(), updatedTicket) == null) {
      log.error("Error when trying to replace value, could not find entry for id: {}", ticketId);
    }
  }

  @Override
  public void assignToTeamMember(UUID ticketId, String assignTo, String modifiedBy) {
    TicketDto ticket = tickets.get(ticketId);
    TicketDto updatedTicket = new TicketDto(
      ticket.getId(),
      assignTo,
      Instant.now(),
      "",//memberID
      "",//referenceID
      ticket.getCreatedBy(),
      ticket.getPriority(),
      ticket.getType(),
      ticket.getRemindNotificationDate(),
      ticket.getRemindNotificationTime(),
      ticket.getRemindMessage(),
      ticket.getDescription(),
      ticket.getStatus()
    );
    if (tickets.replace(updatedTicket.getId(), updatedTicket) == null) {
      log.error("Error when trying to replace value, could not find entry for id: {}", ticketId);
    }
  }

  @Override
  public void changeStatus(UUID ticketId, TicketStatus newStatus, String modifiedBy) {
    TicketDto ticket = tickets.get(ticketId);
    TicketDto updatedTicket = new TicketDto(
      ticket.getId(),
      ticket.getAssignedTo(),
      Instant.now(),
      "",//memberID
      "",//referenceID
      ticket.getCreatedBy(),
      ticket.getPriority(),
      ticket.getType(),
      ticket.getRemindNotificationDate(),
      ticket.getRemindNotificationTime(),
      ticket.getRemindMessage(),
      ticket.getDescription(),
      newStatus
    );
    if (tickets.replace(updatedTicket.getId(), updatedTicket) == null) {
      log.error("Error when trying to replace value, could not find entry for id: {}", ticketId);
    }
  }

  @Override
  public void changeReminder(UUID ticketId, RemindNotification newReminder, String modifiedBy) {
    TicketDto ticket = tickets.get(ticketId);
    TicketDto updatedTicket = new TicketDto(
      ticket.getId(),
      ticket.getAssignedTo(),
      Instant.now(),
      "",//memberID
      "",//referenceID
      ticket.getCreatedBy(),
      ticket.getPriority(),
      ticket.getType(),
      newReminder.getDate(),
      newReminder.getTime(),
      newReminder.getMessage(),
      ticket.getDescription(),
      ticket.getStatus()
    );
    if (tickets.replace(updatedTicket.getId(), updatedTicket) == null) {
      log.error("Error when trying to replace value, could not find entry for id: {}", ticketId);
    }

  }

  @Override
  public void changePriority(UUID ticketId, float newPriority, String modifiedBy) {
    TicketDto ticket = tickets.get(ticketId);
    TicketDto updatedTicket = new TicketDto(
      ticket.getId(),
      ticket.getAssignedTo(),
      Instant.now(),
      "",//memberID
      "",//referenceID
      ticket.getCreatedBy(),
      Math.max(0f, Math.min(newPriority, 1.0f)),
      ticket.getType(),
      ticket.getRemindNotificationDate(),
      ticket.getRemindNotificationTime(),
      ticket.getRemindMessage(),
      ticket.getDescription(),
      ticket.getStatus()
    );
    if (tickets.replace(updatedTicket.getId(), updatedTicket) == null) {
      log.error("Error when trying to replace value, could not find entry for id: {}", ticketId);
    }

  }

  @Override
  public void createTicketFromQuestions(QuestionGroupDTO questionGroupDTO) {
    //TODO("To be implemented")
  }

  @Override
  public void setQuestionGroupAsDone(String groupId, String changedBy) {
    //TODO("To be implemented")

  }

}
