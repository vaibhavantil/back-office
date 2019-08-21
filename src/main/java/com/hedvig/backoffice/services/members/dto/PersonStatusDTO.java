package com.hedvig.backoffice.services.members.dto;

import lombok.Value;

@Value
public class PersonStatusDTO {
  Flag flag;
  Boolean whitelisted;
}
