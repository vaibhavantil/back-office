package com.hedvig.backoffice.services.tickets.dto

import com.hedvig.backoffice.graphql.types.TicketInput

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import java.time.LocalDate
import java.time.LocalTime

data class CreateTicketDto(
    val createdBy: String,
    val assignedTo: String? = null,
    val memberId: String? = null,
    val referenceId: String? = null,
    @Min(0)
    @Max(1)
    val priority: Float? = null,
    val type: TicketType,
    val remindNotificationDate: LocalDate? = null,
    val remindNotificationTime: LocalTime? = null,
    val remindMessage: String? = null,
    val description: String? = null
) {
  companion object {
    fun from(ticketInput: TicketInput, createdBy: String): CreateTicketDto {
      return CreateTicketDto(
        createdBy = createdBy,
        assignedTo = ticketInput.assignedTo,
        priority = ticketInput.priority,
        type = ticketInput.type,
        remindNotificationDate = ticketInput.remindNotificationDate,
        remindNotificationTime = ticketInput.remindNotificationTime,
        remindMessage = ticketInput.remindMessage,
        description = ticketInput.description,
        referenceId = ticketInput.referenceId
      )
    }
  }
}
