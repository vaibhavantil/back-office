package com.hedvig.backoffice.graphql.types

import java.net.URL
import java.time.Instant

data class FileUpload(
  val fileUploadUrl: URL,
  val timestamp: Instant,
  val mimeType: String,
  val memberId: String
)

