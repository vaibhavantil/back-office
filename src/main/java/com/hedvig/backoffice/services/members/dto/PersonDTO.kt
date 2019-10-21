package com.hedvig.backoffice.services.members.dto

import com.hedvig.backoffice.graphql.types.Whitelisted
import lombok.Value
import java.util.Optional

data class PersonDTO(
    val personFlags: List<Flag>,
    val debt: DebtDTO,
    val whitelisted: Whitelisted?,
    val status: PersonStatusDTO
)
