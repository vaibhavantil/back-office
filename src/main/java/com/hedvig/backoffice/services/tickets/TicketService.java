package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.domain.QuestionGroup;
import com.hedvig.backoffice.graphql.types.RemindNotification;
import com.hedvig.backoffice.services.tickets.dto.CreateTicketDto;
import com.hedvig.backoffice.services.tickets.dto.FullTicketHistoryDto;
import com.hedvig.backoffice.services.tickets.dto.TicketDto;
import com.hedvig.backoffice.services.tickets.dto.TicketStatus;

import java.util.List;
import java.util.UUID;

public interface TicketService {

  List<TicketDto> getAllTickets(Boolean resolved);

  TicketDto getTicketById(UUID ticketId);

  FullTicketHistoryDto getTicketWithFullHistory(UUID ticketId);

  UUID createNewTicket(CreateTicketDto ticket, String createdBy);

  void changeDescription(UUID ticketId, String newDescription, String modifiedBy);

  void assignToTeamMember(UUID ticketId, String teamMemberId, String modifiedBy);

  void changeStatus(UUID ticketId, TicketStatus newStatus, String modifiedBy);

  void changeReminder(UUID ticketId, RemindNotification newReminder, String modifiedBy);

  void changePriority(UUID ticketId, float newPriority, String modifiedBy);

  void createTicketFromQuestions (QuestionGroup questionGroup);

  void setQuestionGroupAsDone(String groupId, String changedBy );


}
