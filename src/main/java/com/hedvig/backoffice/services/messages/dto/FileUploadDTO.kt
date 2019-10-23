package com.hedvig.backoffice.services.messages.dto

import java.time.Instant

data class FileUploadDTO(
  var fileUploadKey: String,
  var timestamp: Instant,
  var mimeType: String,
  var memberId: String
)
