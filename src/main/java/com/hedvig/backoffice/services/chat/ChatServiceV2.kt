package com.hedvig.backoffice.services.chat

import com.hedvig.backoffice.services.chat.data.SendMessageResponse
import com.hedvig.backoffice.services.messages.dto.BotMessageDTO

interface ChatServiceV2 {
  fun fetchMessages(memberId: String, personnelEmail: String, personnelToken: String): List<BotMessageDTO>
  fun sendMessage(memberId: String, message: String, forceSendMessage: Boolean, personnelEmail: String, personnelToken: String): SendMessageResponse
  fun sendNotification(memberId: String, personnelToken: String)
}

