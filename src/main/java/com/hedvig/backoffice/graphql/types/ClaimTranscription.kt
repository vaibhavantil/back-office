package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.claims.dto.ClaimTranscription as ClaimTranscriptionDTO

data class ClaimTranscription(
  val text: String,
  val confidenceScore: Float
) {
  companion object {
    fun fromDTO(dto: ClaimTranscriptionDTO): ClaimTranscription? {
      return ClaimTranscription(dto.text, dto.confidenceScore)
    }
  }
}
