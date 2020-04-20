package com.hedvig.backoffice.services.itemizer.dto.request

import java.util.UUID

data class AddItemBrandRequest(
    val name: String,
    val itemTypeId: UUID,
    val itemCompanyId: UUID
)
