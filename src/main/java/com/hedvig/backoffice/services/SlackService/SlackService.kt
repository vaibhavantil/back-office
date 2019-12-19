package com.hedvig.backoffice.services.SlackService

import com.hedvig.backoffice.graphql.types.RemindNotification
import com.hedvig.backoffice.services.SlackService.dto.ReminderData
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap

@Component
class SlackService(val slackServiceClient: SlackServiceClient,
                   @Value("\${hedvig.slack.token}") val token: String,
                   @Value("\${iexMembers}") val iexMembers: String,
                   @Value("\${hedvig.slack.channel}") val channel: String) {

  fun addTicketReminder(ticketId: UUID, reminderInfo: RemindNotification): ResponseEntity<String> {
    val slackId = matchEmailToSlackId(reminderInfo.sendReminderTo)
    val date = LocalDateTime.of(reminderInfo.date, reminderInfo.time).atZone(ZoneId.of("Europe/Stockholm")).toInstant().epochSecond

    return this.slackServiceClient.sendNotification(
      token,
      ReminderData(
      channel = channel,
      text = "<@$slackId> ${reminderInfo.message}",
      post_at = date
    ))
  }

  private fun matchEmailToSlackId(email: String): String? {
    val slackName = getSlackName()

    if(!slackName.keys.contains(email)) {
      throw RuntimeException("This email address $email doesn't seem to match any of our team members")
    }
    return slackName[email]
  }

  private fun getSlackName(): MutableMap<String, String> {
    val listOfTeamMembers = iexMembers.split(",")
    val emailToSlackName = HashMap<String, String>()

    listOfTeamMembers.forEach { member ->
      val memberData = member.split(":")
      emailToSlackName.put(memberData[0], memberData[1])
    }
    return emailToSlackName
  }
}
