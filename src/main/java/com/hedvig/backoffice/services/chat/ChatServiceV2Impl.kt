package com.hedvig.backoffice.services.chat

import com.hedvig.backoffice.config.feign.ExternalServiceBadRequestException
import com.hedvig.backoffice.config.feign.ExternalServiceException
import com.hedvig.backoffice.services.UploadedFilePostprocessor
import com.hedvig.backoffice.services.chat.data.SendMessageResponse
import com.hedvig.backoffice.services.expo.ExpoNotificationService
import com.hedvig.backoffice.services.messages.BotService
import com.hedvig.backoffice.services.notificationService.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ChatServiceV2Impl(
    private val botService: BotService,
    private val messagePostProcessor: UploadedFilePostprocessor,
    private val notificationService: NotificationService,
    private val expoNotificationService: ExpoNotificationService
) : ChatServiceV2 {
  override fun fetchMessages(memberId: String, personnelEmail: String, personnelToken: String): List<String> {
    val botServiceMessages = botService.messages(memberId, personnelToken)
    botServiceMessages.forEach { messagePostProcessor.processMessage(it) }
    return botServiceMessages.map { it.toJson() }
  }

  override fun sendMessage(memberId: String, message: String, forceSendMessage: Boolean, personnelId: String, personnelToken: String): SendMessageResponse {
    try {
      botService.response(memberId, message, forceSendMessage, personnelToken)
      sendNotification(memberId, personnelToken)
    } catch (e: ExternalServiceBadRequestException) {
      logger.error("chat not updated memberId = $memberId", e)
      return SendMessageResponse.Failed(memberId, 400, e.message)
    } catch (e: ExternalServiceException) {
      logger.error("chat not updated memberId = $memberId", e)
      return SendMessageResponse.Failed(memberId, 5000, e.message)
    }
    return SendMessageResponse.Successful(memberId)
  }

  override fun sendNotification(memberId: String, personnelToken: String) {
    if (notificationService.getFirebaseToken(memberId).isPresent) {
      notificationService.sendPushNotification(memberId)
      return
    }
    expoNotificationService.sendNotification(memberId, personnelToken)
  }

  companion object {
    private val logger = LoggerFactory.getLogger(ChatServiceV2Impl::class.java)
  }
}
