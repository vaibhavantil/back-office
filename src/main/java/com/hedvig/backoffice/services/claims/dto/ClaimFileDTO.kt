package com.hedvig.backoffice.services.claims.dto

import java.time.Instant
import java.util.*

data class ClaimFileDTO (
  val claimFileId: UUID,
  val bucket: String,
  val key: String,
  val claimId: String,
  val contentType: String,
  val uploadedAt: Instant,
  val fileName: String,
  val category: String?
)
