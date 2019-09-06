package com.hedvig.backoffice.services.members.dto;

import com.hedvig.backoffice.graphql.types.Whitelisted;
import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
public class PersonDTO {
  List<Flag> personFlags;
  DebtDTO debt;
  Optional<Whitelisted> whitelisted;
  PersonStatusDTO status;
}
