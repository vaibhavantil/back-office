package com.hedvig.backoffice.services.tickets.dto

import java.time.Instant
import java.util.*

data class TicketHistoryDto(
    val id: UUID,
    val createdAt: Instant,
    val createdBy: String,
    val type: TicketType,
    val revisions: List<TicketRevisionDto>
)
