package com.hedvig.backoffice.services.tickets

import com.hedvig.backoffice.domain.QuestionGroup
import com.hedvig.backoffice.graphql.types.RemindNotification
import com.hedvig.backoffice.services.tickets.dto.CreateTicketDto
import com.hedvig.backoffice.services.tickets.dto.TicketHistoryDto
import com.hedvig.backoffice.services.tickets.dto.TicketDto
import com.hedvig.backoffice.services.tickets.dto.TicketStatus
import java.util.UUID

interface TicketService {

    fun getAllTickets(resolved: Boolean?): List<TicketDto>

    fun getTicketById(ticketId: UUID): TicketDto

    fun getTicketHistory(ticketId: UUID): TicketHistoryDto

    fun createTicket(ticket: CreateTicketDto, createdBy: String): UUID

    fun changeDescription(ticketId: UUID, newDescription: String, modifiedBy: String)

    fun changeAssignedTo(ticketId: UUID, assignedTo: String, modifiedBy: String)

    fun changeStatus(ticketId: UUID, newStatus: TicketStatus, modifiedBy: String)

    fun changeReminder(ticketId: UUID, newReminder: RemindNotification, modifiedBy: String)

    fun changePriority(ticketId: UUID, newPriority: Float, modifiedBy: String)
}
