package com.hedvig.backoffice.services.claims.dto

import java.time.Instant
import java.util.*

data class ClaimFileDTO (
  val claimFileId: UUID,
  val bucket: String,
  val key: String,
  val claimId: String,
  val contentType: String,
  val fileName: String,
  val markedAsDeleted: Boolean,
  val markedAsDeletedBy: String?,
  val markedAsDeletedAt: Instant?,
  val category: String?
)
