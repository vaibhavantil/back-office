package com.hedvig.backoffice.graphql.types;

import lombok.Value;

@Value
public class DirectDebitStatus {
  String memberId;
  Boolean activated;
}
