package com.hedvig.backoffice.services.messages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotMessageHeaderDTO {
  public Long messageId;
  public Long fromId;
  public Long timeStamp; // Time when sent/recieved on API-GW
  public String loadingIndicator; // Link to animation to show during load
  public String avatarName; // Link to avatar animation to show over message
  public Long pollingInterval; // Frequency of next request
  public String statusMessage;
  public Boolean richTextChatCompatible;
  public Boolean shouldRequestPushNotifications; // Should responding to this message prompt user to turn on push notifications
  public boolean editAllowed;
}
