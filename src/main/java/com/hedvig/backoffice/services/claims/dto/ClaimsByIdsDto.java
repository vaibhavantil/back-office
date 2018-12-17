package com.hedvig.backoffice.services.claims.dto;

import java.util.List;
import java.util.UUID;
import lombok.Value;

@Value
public class ClaimsByIdsDto {
  List<UUID> ids;
}
