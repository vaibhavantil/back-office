package com.hedvig.backoffice.services.underwriter.dtos

import java.time.Instant
import java.util.LinkedList
import java.util.UUID

class SignedQuoteResponseDto(
  val id: UUID,
  val signedAt: Instant
){
  companion object {
    fun from(linkedList: LinkedHashMap<String, String>): SignedQuoteResponseDto{
      return SignedQuoteResponseDto(
        UUID.fromString(linkedList["id"]),
        Instant.parse(linkedList["signedAt"])
      )
    }
  }
}

