package com.hedvig.backoffice.services.SlackService.dto

data class ReminderData(
  val channel: String,
  val text: String,
  val post_at: Long
)
