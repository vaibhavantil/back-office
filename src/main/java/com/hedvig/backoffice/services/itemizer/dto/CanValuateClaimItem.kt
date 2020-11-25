package com.hedvig.backoffice.services.itemizer.dto

import java.util.UUID

data class CanValuateClaimItem(
    val canValuate: Boolean,
    val typeOfContract: String?,
    val itemFamily: String?,
    val itemTypeId: UUID?
)
