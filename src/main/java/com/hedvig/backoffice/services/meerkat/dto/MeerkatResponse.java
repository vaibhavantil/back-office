package com.hedvig.backoffice.services.meerkat.dto;

import lombok.Value;

@Value
public class MeerkatResponse {

  private String query;
  private SanctionStatus result;
}
