package com.hedvig.backoffice.services.itemizer.dto.request

import java.util.UUID

data class AddItemModelRequest(
    val name: String,
    val itemBrandId: UUID
)
