package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.graphql.types.RemindNotification;
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;
import com.hedvig.backoffice.services.questions.dto.QuestionDTO;
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
  public void createTicketFromQuestions(QuestionGroupDTO questionGroup) {
    List<QuestionDTO> questions = questionGroup.getQuestions();
    if (questions.isEmpty()) {
      return;
    }

    int lastQuestionIndex = 0;
    Long lastDate = 0L;
    for (int i = 0; i <questions.size() ; i++ ){
      if( questions.get(i).getDate() > lastDate) {
        lastDate = questions.get(i).getDate();
        lastQuestionIndex = i;
      }
    }
    QuestionDTO lastQuestion = questions.get(lastQuestionIndex);
    //TODO:("Make a big string out of everything and put it in description?")

      CreateTicketDto ticketDto = new CreateTicketDto(
        "question-service",
        "Unassigned",
        questionGroup.getMemberId().toString(),
        questionGroup.getId().toString(),
        null,
        TicketType.MESSAGE,
        null, null, null,
        lastQuestion.getMessage().toString(),
        TicketStatus.WAITING
    );

      ticketServiceClient.createTicket(ticketDto);

  }
}
