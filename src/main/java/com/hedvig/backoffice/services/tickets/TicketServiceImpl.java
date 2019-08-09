package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.domain.Question;
import com.hedvig.backoffice.domain.QuestionGroup;
import com.hedvig.backoffice.graphql.types.RemindNotification;
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;
import com.hedvig.backoffice.services.tickets.dto.*;

import java.util.*;
import java.util.stream.Collectors;

public class TicketServiceImpl implements TicketService {
  private final TicketServiceClient ticketServiceClient;

  public TicketServiceImpl(TicketServiceClient ticketServiceClient) {
    this.ticketServiceClient = ticketServiceClient;
  }

  /**
   * getAllTicket
   * @param onlyResolvedTickets
   * When null we fetch all tickets (tickets of all TicketStatus)
   * If true we fetch only tickets of TicketStatus.RESOLVED
   * If false we fetch tickets of _both_ TicketStatus.WAITING and TicketStatus.WORKED_ON
   */

  @Override
  public List<TicketDto> getAllTickets(Boolean onlyResolvedTickets) {
    if (onlyResolvedTickets == null) {
      return this.ticketServiceClient.getTickets();
    } else if (onlyResolvedTickets) {
      return this.ticketServiceClient.getResolvedTickets();
    }
      return this.ticketServiceClient.getUnresolvedTickets();
  }

  @Override
  public TicketDto getTicketById(UUID ticketId) {
    return ticketServiceClient.getTicket(ticketId);
  }

  @Override
  public FullTicketHistoryDto getTicketWithFullHistory(UUID ticketId) {
    return ticketServiceClient.getTicketWithFullHistory(ticketId);
  }

  @Override
  public UUID createNewTicket(CreateTicketDto ticket, String createdBy) {
    return this.ticketServiceClient.createTicket(ticket);
  }

  @Override
  public void changeDescription(UUID ticketId, String newDescription, String modifiedBy) {
    this.ticketServiceClient.changeDescription(ticketId, new ChangeDescriptionDto( newDescription,  modifiedBy));
  }

  @Override
  public void assignToTeamMember(UUID ticketId, String assignTo, String modifiedBy) {
    this.ticketServiceClient.changeAssignedTo(ticketId,new ChangeAssignToDto(assignTo, modifiedBy));
  }

  @Override
  public void changeStatus(UUID ticketId, TicketStatus newStatus, String modifiedBy) {
    this.ticketServiceClient.changeStatus(ticketId, new ChangeStatusDto(newStatus, modifiedBy));
  }

  @Override
  public void changeReminder(UUID ticketId, RemindNotification newReminder, String modifiedBy) {
    this.ticketServiceClient.changeReminder(ticketId, new ChangeReminderDto( newReminder, modifiedBy));
  }

  @Override
  public void changePriority(UUID ticketId, float newPriority, String modifiedBy) {
    this.ticketServiceClient.changePriority(ticketId, new ChangePriorityDto( newPriority, modifiedBy));
  }

  @Override
  public void createTicketFromQuestions(QuestionGroup questionGroup) {
    List<Question> questions = new ArrayList<Question>();

    questionGroup.getQuestions().addAll(questions);
    if (questions.isEmpty()) {
      return;
    }
    Question lastQuestion = questions.stream()
      .sorted(Comparator.comparing(Question::getDate).reversed())
      .collect(Collectors.toList()).get(0);

      //We do this only to get the associated MemberID =/
    QuestionGroupDTO questionGroupDto = QuestionGroupDTO.fromDomain(questionGroup);


    //TODO:("Make a big string out of everything and put it in description?")
      CreateTicketDto ticketDto = new CreateTicketDto(
        "question-service",
        "Unassigned",
        questionGroupDto.getMemberId(),
        questionGroup.getId().toString(),
        null,
        TicketType.MESSAGE,
        null, null, null,
        lastQuestion.getMessage(),
        TicketStatus.WAITING
    );

      ticketServiceClient.createTicketFromQuestion(ticketDto);
  }

  @Override
  public void setQuestionGroupAsDone(String groupId, String changedBy) {
    ChangeStatusDto newStatus = new ChangeStatusDto(TicketStatus.RESOLVED, changedBy);
    ticketServiceClient.changeStatusThroughRefId(groupId, newStatus );
  }
}
