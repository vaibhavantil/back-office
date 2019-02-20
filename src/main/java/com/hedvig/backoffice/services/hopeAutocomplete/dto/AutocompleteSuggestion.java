package com.hedvig.backoffice.services.hopeAutocomplete.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AutocompleteSuggestion {
  public double score;
  public String text;
}
