package com.hedvig.backoffice.graphql.types;

import lombok.Value;
import java.time.Instant;

@Value
public class Whitelisted {
  Instant whitelistedAt;
  String whitelistedBy;
}
