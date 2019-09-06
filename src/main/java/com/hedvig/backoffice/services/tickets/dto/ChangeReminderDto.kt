package com.hedvig.backoffice.services.tickets.dto

import com.hedvig.backoffice.graphql.types.RemindNotification
import lombok.Value

data class ChangeReminderDto(
    val reminder: RemindNotification,
    val changedBy: String
)
