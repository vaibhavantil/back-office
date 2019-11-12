package com.hedvig.backoffice.services.claims.dto

import java.util.*

data class ClaimFileDTO (
  val claimFileId: UUID,
  val bucket: String?,
  val key: String,
  val claimId: UUID?,
  val contentType: String,
  val data: ByteArray,
  val fileName: String,
  val imageId: UUID?,
  val metaInfo: String,
  val size: Long,
  val userId: String?
)
