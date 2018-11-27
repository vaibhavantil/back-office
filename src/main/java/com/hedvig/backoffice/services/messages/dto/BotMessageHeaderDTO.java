package com.hedvig.backoffice.services.messages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotMessageHeaderDTO {
  Long messageId;
  Long fromId;
  Long timeStamp; // Time when sent/recieved on API-GW
  String loadingIndicator; // Link to animation to show during load
  String avatarName; // Link to avatar animation to show over message
  Long pollingInterval; // Frequency of next request
  String statusMessage;
  Boolean richTextChatCompatible;
  Boolean shouldRequestPushNotifications; // Should responding to this message prompt user to turn on push notifications
  boolean editAllowed;
}
