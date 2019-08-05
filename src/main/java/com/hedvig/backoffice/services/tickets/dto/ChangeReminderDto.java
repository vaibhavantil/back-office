package com.hedvig.backoffice.services.tickets.dto;

import com.hedvig.backoffice.graphql.types.RemindNotification;
import lombok.Value;

@Value
public class ChangeReminderDto {
  private RemindNotification reminder;
  private String changedBy;
}
