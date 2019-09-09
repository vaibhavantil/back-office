package com.hedvig.backoffice.services.tickets

import com.hedvig.backoffice.domain.QuestionGroup
import com.hedvig.backoffice.graphql.types.RemindNotification
import com.hedvig.backoffice.services.messages.dto.BotMessageDTO
import com.hedvig.backoffice.services.tickets.dto.CreateMessageTicketDto
import com.hedvig.backoffice.services.tickets.dto.*
import org.jvnet.hk2.annotations.Service
import org.springframework.beans.factory.annotation.Autowired

import java.util.*

@Service
class TicketServiceImpl @Autowired constructor(
  private val ticketServiceClient: TicketServiceClient
) : TicketService {

    override fun getAllTickets(resolved: Boolean?): List<TicketDto> {
        if (resolved == null) {
            return this.ticketServiceClient.getAllTickets()
        } else if (resolved) {
            return this.ticketServiceClient.getResolvedTickets()
        }
        return this.ticketServiceClient.getUnresolvedTickets()
    }

    override fun getTicketById(ticketId: UUID): TicketDto {
        return ticketServiceClient.getTicket(ticketId)
    }

    override fun getTicketHistory(ticketId: UUID): TicketHistoryDto {
        return ticketServiceClient.getTicketHistory(ticketId)
    }

    override fun createTicket(ticket: CreateTicketDto, createdBy: String): UUID {
        return this.ticketServiceClient.createTicket(ticket)
    }

    override fun changeDescription(ticketId: UUID, newDescription: String, modifiedBy: String) {
        this.ticketServiceClient.changeDescription(ticketId, ChangeDescriptionDto(newDescription, modifiedBy))
    }

    override fun changeAssignedTo(ticketId: UUID, assignTo: String, modifiedBy: String) {
        this.ticketServiceClient.changeAssignedTo(ticketId, ChangeAssignToDto(assignTo, modifiedBy))
    }

    override fun changeStatus(ticketId: UUID, newStatus: TicketStatus, modifiedBy: String) {
        this.ticketServiceClient.changeStatus(ticketId, ChangeStatusDto(newStatus, modifiedBy))
    }

    override fun changeReminder(ticketId: UUID, newReminder: RemindNotification, modifiedBy: String) {
        this.ticketServiceClient.changeReminder(ticketId, ChangeReminderDto(newReminder, modifiedBy))
    }

    override fun changePriority(ticketId: UUID, newPriority: Float, modifiedBy: String) {
        this.ticketServiceClient.changePriority(ticketId, ChangePriorityDto(newPriority, modifiedBy))
    }

    override fun createTicketFromQuestions(questionGroup: QuestionGroup) {

        if (questionGroup.questions.isEmpty()) {
          return
        }

        val lastQuestion = questionGroup.questions.toList().maxBy { question -> question.date }!!

        val lastQuestionText = BotMessageDTO.fromJson(lastQuestion.message).body.path("text").textValue()

        val messageTicket = CreateMessageTicketDto(
            createdBy = "back-office",
            memberId = questionGroup.subscription.memberId,
            groupId = questionGroup.id.toString(),
            text = lastQuestionText
        )

        ticketServiceClient.createMessageTicket(messageTicket)
    }

    override fun setQuestionGroupAsDone(groupId: String) {
        ticketServiceClient.resolveMessageTicket(groupId)
    }
}
