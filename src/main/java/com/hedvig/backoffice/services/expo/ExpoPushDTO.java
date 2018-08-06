package com.hedvig.backoffice.services.expo;

import lombok.Value;

@Value
class ExpoPushDTO {
  private String to;
  private String title;
  private String body;
}
