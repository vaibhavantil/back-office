package com.hedvig.backoffice.graphql.types

import java.net.URL

data class ClaimFileUpload(
   val claimFileId: String,
   val fileUploadUrl: URL,
   val claimId: String,
   val markedAsDeleted: Boolean
)
