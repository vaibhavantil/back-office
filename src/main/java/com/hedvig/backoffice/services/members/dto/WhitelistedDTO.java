package com.hedvig.backoffice.services.members.dto;

import lombok.Value;
import java.time.Instant;

@Value
public class WhitelistedDTO {
  Instant whitelistedAt;
  String whitelistedBy;
}
