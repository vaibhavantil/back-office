package com.hedvig.backoffice.services.chat

import com.hedvig.backoffice.services.chat.data.SendMessageResponse

interface ChatServiceV2 {
  fun fetchMessages(memberId: String, personnelEmail: String, personnelToken: String): List<String>
  fun sendMessage(memberId: String, message: String, forceSendMessage: Boolean, personnelEmail: String, personnelToken: String): SendMessageResponse
  fun sendNotification(memberId: String, personnelToken: String)
}

