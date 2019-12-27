package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.tickets.dto.TicketType
import java.time.LocalDate
import java.time.LocalTime
import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class TicketInput(
    val assignedTo: String? = null,
    @Min(0)
    @Max(1)
    val priority: Float = 0f,
    val type: TicketType,
    val remindNotificationDate: LocalDate? = null,
    val remindNotificationTime: LocalTime? = null,
    val remindMessage: String? = null,
    val description: String? = null,
    val referenceId: String?
)

