package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.chat.data.SendMessageResponse as ChatServiceV2SendMessageResponse
import com.hedvig.backoffice.graphql.UnionType

sealed class SendMessageResponse {
  @UnionType
  data class SendMessageSuccessful(
    val memberId: String
  ): SendMessageResponse()
  @UnionType
  data class SendMessageFailed(
    val memberId: String,
    val errorCode: Int,
    val errorMessage: String?
  ): SendMessageResponse()

  companion object {
    fun from(response: ChatServiceV2SendMessageResponse) = when (response) {
      is ChatServiceV2SendMessageResponse.Successful -> SendMessageSuccessful(response.memberId)
      is ChatServiceV2SendMessageResponse.Failed -> SendMessageFailed(
        response.memberId,
        response.errorCode,
        response.errorMessage
      )
    }
  }
}
