package com.hedvig.backoffice.services.claims.dto

data class ClaimTranscription(
  val text: String,
  val confidenceScore: Float,
  val languageCode: String
)
