package com.hedvig.backoffice.services.members.dto;

import java.util.List;
import lombok.Value;

@Value
public class ChargeMembersDTO {
  private List<String> memberIds;
}
