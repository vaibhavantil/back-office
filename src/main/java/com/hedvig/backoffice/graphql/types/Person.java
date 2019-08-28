package com.hedvig.backoffice.graphql.types;

import com.hedvig.backoffice.services.members.dto.DebtDTO;
import com.hedvig.backoffice.services.members.dto.Flag;
import com.hedvig.backoffice.services.members.dto.PersonStatusDTO;

import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
public class Person {
  List<Flag> personFlags;
  DebtDTO debt;
  Optional<Whitelisted> whitelisted;
  PersonStatusDTO status;
}
