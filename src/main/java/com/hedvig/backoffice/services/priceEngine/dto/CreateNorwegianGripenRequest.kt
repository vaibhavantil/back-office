package com.hedvig.backoffice.services.priceEngine.dto

data class CreateNorwegianGripenRequest(
  val baseFactorString: String,
  val factors: List<NorwegianGripenFactorRequest>
)
