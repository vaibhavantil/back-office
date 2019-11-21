package com.hedvig.backoffice.graphql.types

import java.net.URL
import java.util.*

data class ClaimFileUpload(
  val claimFileId: UUID,
  val fileUploadUrl: URL,
  val claimId: String,
  val markedAsDeleted: Boolean,
  val category: String?
)
