package com.hedvig.backoffice.services.claims.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClaimField {
  @NotNull private String name;

  @NotNull private String title;

  @NotNull private String type;
}
