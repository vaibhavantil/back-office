package com.hedvig.backoffice.services.tickets.dto

data class ChangeStatusDto(
    val status: TicketStatus,
    val changedBy: String
)
