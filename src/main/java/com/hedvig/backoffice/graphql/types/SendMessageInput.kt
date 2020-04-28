package com.hedvig.backoffice.graphql.types

data class SendMessageInput(
  val memberId: String,
  val message: String,
  val forceSendMessage: Boolean
)
