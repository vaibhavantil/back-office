package com.hedvig.backoffice.graphql.types

import java.net.URL
import java.util.*

data class ClaimFileUpload(
   val fileUploadUrl: URL,
   val claimId: UUID
)
