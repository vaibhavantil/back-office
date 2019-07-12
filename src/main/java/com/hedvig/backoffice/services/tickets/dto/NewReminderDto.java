package com.hedvig.backoffice.services.tickets.dto;

import com.hedvig.backoffice.graphql.types.RemindNotification;
import lombok.Value;

@Value
public class NewReminderDto {
  RemindNotification reminder;
  String modifiedBy;
}
