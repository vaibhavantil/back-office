package com.hedvig.backoffice.services.tickets.dto

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class ChangePriorityDto(
    @Min(0)
    @Max(1)
    val priority: Float,
    val changedBy: String
)
