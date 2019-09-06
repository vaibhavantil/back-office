package com.hedvig.backoffice.services.tickets.dto

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

data class TicketRevisionDto(
  val assignedTo: String? = null,
  val manualPriority: Float? = null,
  val remindDate: LocalDate? = null,
  val remindTime: LocalTime? = null,
  val remindMessage: String? = null,
  val status: TicketStatus,
  val changedAt: Instant,
  val changeType: TicketChangeType,
  val changedBy: String,
  val description: String? = null
)
