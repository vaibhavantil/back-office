package com.hedvig.backoffice.services.priceEngine.dto

data class
NorwegianGripenFactorRequest(
  val factorType: NorwegianGripenFactorType,
  val factorString: String
)
