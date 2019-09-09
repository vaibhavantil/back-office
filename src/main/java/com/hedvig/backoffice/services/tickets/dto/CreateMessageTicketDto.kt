package com.hedvig.backoffice.services.tickets.dto

data class CreateMessageTicketDto(
  val createdBy: String,
  val memberId: String,
  val groupId: String,
  val text: String?
)
