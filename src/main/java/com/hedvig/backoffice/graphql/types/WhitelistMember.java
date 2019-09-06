package com.hedvig.backoffice.graphql.types;

import lombok.*;

@Value
public class WhitelistMember {
  String memberId;
  String approverEmail;
}

