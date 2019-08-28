package com.hedvig.backoffice.graphql.types;

import com.hedvig.backoffice.services.members.dto.Flag;
import lombok.Value;

@Value
public class PersonStatus {
  Flag flag;
  Boolean whitelisted;
}
