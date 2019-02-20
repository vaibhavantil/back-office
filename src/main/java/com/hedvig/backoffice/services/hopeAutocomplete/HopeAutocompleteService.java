package com.hedvig.backoffice.services.hopeAutocomplete;

import com.hedvig.backoffice.services.hopeAutocomplete.dto.AutocompleteSuggestion;
import com.hedvig.backoffice.services.hopeAutocomplete.dto.SelectSuggestionRequestBodyDto;

import java.util.List;

public interface HopeAutocompleteService {
  List<AutocompleteSuggestion> getAutocompleteSuggestions(String query);

  void selectSuggestion(SelectSuggestionRequestBodyDto requestBody);
}
