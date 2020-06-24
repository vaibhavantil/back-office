package com.hedvig.backoffice.services.chat.data

sealed class SendMessageResponse {
  data class Successful(
    val memberId: String
  ): SendMessageResponse()
  data class Failed(
    val memberId: String,
    val errorCode: Int,
    val errorMessage: String?
  ): SendMessageResponse()
}
