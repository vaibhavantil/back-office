package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.claims.dto.ClaimFileDTO
import java.time.Instant
import java.util.*

data class ClaimFile (
  val claimFileId: UUID,
  val bucket: String,
  val key: String,
  val claimId: String,
  val contentType: String,
  val UploadedAt: Instant,
  val fileName: String,
  val category: String?
) {
  companion object{
    fun fromDTO(dto: ClaimFileDTO): ClaimFile {
      return ClaimFile(
        dto.claimFileId,
        dto.bucket,
        dto.key,
        dto.claimId,
        dto.contentType,
        dto.uploadedAt,
        dto.fileName,
        dto.category
      )
    }
  }
}

