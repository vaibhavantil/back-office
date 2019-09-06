package com.hedvig.backoffice.services.tickets.dto

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class TicketDto(
    val id: UUID,
    val assignedTo: String? = null,
    val createdAt: Instant,
    val memberId: String? = null,
    val referenceId: String? = null,
    val createdBy: String,
    @Min(0)
    @Max(1)
    val priority: Float = 0f,
    val type: TicketType,
    val remindNotificationDate: LocalDate? = null,
    val remindNotificationTime: LocalTime? = null,
    val remindMessage: String? = null,
    val description: String? = null,
    val status: TicketStatus
)
