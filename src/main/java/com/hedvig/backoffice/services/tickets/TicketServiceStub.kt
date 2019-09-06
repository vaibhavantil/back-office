package com.hedvig.backoffice.services.tickets

import com.hedvig.backoffice.domain.QuestionGroup
import com.hedvig.backoffice.graphql.types.RemindNotification
import com.hedvig.backoffice.services.tickets.dto.*
import lombok.extern.slf4j.Slf4j

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.util.Random
import java.util.UUID

@Slf4j
class TicketServiceStub : TicketService {

  private val teamMembers = listOf(
    "matilda@hedvig.com",
    "karl@hedvig.com",
    "johanna@hedvig.com",
    "tomas@hedvig.com",
    "kalle@hedvig.com",
    "emma@hedvig.com",
    "sara@hedvig.com",
    "axel@hedvig.com",
    "Unassigned"
  )

  private val types = listOf(
    TicketType.CALL_ME,
    TicketType.CLAIM,
    TicketType.MESSAGE,
    TicketType.OTHER,
    TicketType.REMIND
  )

  private val tickets = mutableMapOf<UUID, TicketDto>()

  private val random = Random()

  init {
    for (i in 0 until 10) {
      val id = UUID.randomUUID()
      val ticket = TicketDto(
        id = id,
        assignedTo = teamMembers[i % teamMembers.size],
        createdAt = Instant.now(),
        createdBy = teamMembers[i % teamMembers.size],
        priority = random.nextFloat(),
        type = types[i % types.size],
        remindNotificationDate = LocalDate.now(),
        remindNotificationTime = LocalTime.now(),
        remindMessage = "A message to remind you...",
        description = "A short description of the ticket contents",
        status = TicketStatus.WAITING
      )
      tickets[id] = ticket
    }
  }

    override fun getAllTickets(resolved: Boolean?): List<TicketDto> {
        if (resolved == null) {
            return tickets.values.toList()
        } else if (resolved) {
            return tickets.values.filter { ticket -> ticket.status == TicketStatus.RESOLVED }
        }

        return tickets.values.filter { ticket -> ticket.status != TicketStatus.RESOLVED }
    }

    override fun getTicketById(ticketId: UUID): TicketDto {
        return tickets[ticketId]!!
    }

    override fun getTicketHistory(ticketId: UUID): TicketHistoryDto {
        throw NotImplementedError("Stub for TicketHistory not implemented")
    }

    override fun createTicket(ticket: CreateTicketDto, createdBy: String): UUID {
        val id = UUID.randomUUID()
        val newTicket = TicketDto(
          id = id,
          assignedTo = ticket.assignedTo,
          createdAt = Instant.now(),
          memberId = "",
          referenceId = "",
          createdBy = createdBy,
          priority = ticket.priority!!,
          type = ticket.type,
          remindNotificationDate = ticket.remindNotificationDate,
          remindNotificationTime = ticket.remindNotificationTime,
          remindMessage = ticket.remindMessage,
          description = ticket.description,
          status = TicketStatus.WAITING
        )
        tickets[id] = newTicket
        return id
    }

    override fun changeDescription(ticketId: UUID, newDescription: String, modifiedBy: String) {
        tickets[ticketId] = tickets[ticketId]!!.copy(description = newDescription)
    }

    override fun changeAssignedTo(ticketId: UUID, assignedTo: String, modifiedBy: String) {
        tickets[ticketId] = tickets[ticketId]!!.copy(assignedTo = assignedTo)
    }

    override fun changeStatus(ticketId: UUID, newStatus: TicketStatus, modifiedBy: String) {
        tickets[ticketId] = tickets[ticketId]!!.copy(status = newStatus)
    }

    override fun changeReminder(ticketId: UUID, newReminder: RemindNotification, modifiedBy: String) {
        tickets[ticketId] = tickets[ticketId]!!.copy(
          remindNotificationDate = newReminder.date,
          remindNotificationTime = newReminder.time,
          remindMessage = newReminder.message
        )
    }

    override fun changePriority(ticketId: UUID, newPriority: Float, modifiedBy: String) {
        tickets[ticketId] = tickets[ticketId]!!.copy(priority = newPriority)
    }

    override fun createTicketFromQuestions(questionGroup: QuestionGroup) {
        val lastMessage = questionGroup.questions.last().message
        val messageTicketMaybe = getTicketOfMessageGroup(questionGroup.id.toString())
        val ticketId = when (messageTicketMaybe) {
          null -> UUID.randomUUID()
          else -> messageTicketMaybe.id
        }
        if (messageTicketMaybe != null) {
          val currentDescription = tickets[ticketId]!!.description
          tickets[ticketId] = tickets[ticketId]!!.copy(description = "${currentDescription}\n$lastMessage")
        }
        val newTicket = TicketDto(
            id = ticketId,
            createdAt = Instant.now(),
            memberId = questionGroup.subscription.memberId,
            referenceId = questionGroup.id.toString(),
            createdBy = "back-office",
            priority = 0.5f,
            type = TicketType.MESSAGE,
            description = lastMessage,
            status = TicketStatus.WAITING
        )
        tickets[ticketId] = newTicket
    }

    override fun setQuestionGroupAsDone(groupId: String) {
      val ticket = getTicketOfMessageGroup(groupId)
        ?: return
      tickets[ticket.id] = ticket.copy(status = TicketStatus.RESOLVED)
    }

    private fun getTicketOfMessageGroup(groupId: String): TicketDto? = tickets.values.find { ticket -> ticket.referenceId == groupId }
}
