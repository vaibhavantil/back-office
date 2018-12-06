package com.hedvig.backoffice.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TzHelper {
  public static final ZoneId SWEDEN_TZ = ZoneId.of("Europe/Stockholm");

  public static Instant toInstant(LocalDateTime ldt, ZoneId tz) {
    return (ldt != null) ? ldt.atZone(tz).toInstant() : null;
  }

  public static LocalDateTime toLocalDateTime(Instant ins, ZoneId tz) {
    return (ins != null) ? ins.atZone(tz).toLocalDateTime() : null;
  }

  public static LocalDateTime toLocalDateTime(Long epochMillis, ZoneId tz) {
    return (epochMillis != null) ? Instant.ofEpochMilli(epochMillis).atZone(tz).toLocalDateTime() : null;
  }
}
