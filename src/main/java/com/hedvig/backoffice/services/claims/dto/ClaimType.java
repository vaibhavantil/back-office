package com.hedvig.backoffice.services.claims.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class ClaimType {
  @NotNull private String name;

  @NotNull private String title;

  @NotNull private List<ClaimField> requiredData;

  @NotNull private List<ClaimField> optionalData;
}
