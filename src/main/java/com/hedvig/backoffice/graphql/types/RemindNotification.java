package com.hedvig.backoffice.graphql.types;

import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class RemindNotification {
  LocalDate date;
  LocalTime time;
  String message;
}
