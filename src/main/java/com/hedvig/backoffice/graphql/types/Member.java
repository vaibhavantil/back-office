package com.hedvig.backoffice.graphql.types;

import lombok.Value;

@Value
public class Member {
  String memberId;
  String firstName;
  String lastName;
}
