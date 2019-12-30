package com.hedvig.backoffice.graphql.types

import java.time.LocalDate
import java.time.LocalTime

data class RemindNotification(
    val date: LocalDate,
    val time: LocalTime,
    val message: String,
    val sendReminderTo: String
)
