package com.hedvig.backoffice.graphql.types;

import lombok.Value;

@Value
public class ClaimNoteInput {
  String text;
  String url;
}
