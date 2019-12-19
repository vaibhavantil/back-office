package com.hedvig.backoffice.services.SlackService

import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.SlackService.dto.ReminderData
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(
  name = "slackServiceClient",
  url = "\${hedvig.slack.url}",
  configuration = [FeignConfig::class]
)
interface SlackServiceClient {

  @RequestMapping(method = [RequestMethod.POST], consumes = ["application/json; charset=utf-8"])
  fun sendNotification(
    @RequestHeader("Authorization") token: String,
    @RequestBody req: ReminderData
  ): ResponseEntity<String>
}

