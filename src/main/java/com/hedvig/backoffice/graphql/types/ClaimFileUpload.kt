package com.hedvig.backoffice.graphql.types

import java.net.URL
import java.time.Instant
import java.util.*

data class ClaimFileUpload(
  val claimFileId: UUID,
  val fileUploadUrl: URL,
  val uploadedAt: Instant,
  val claimId: String,
  val category: String?,
  val contentType: String
)
