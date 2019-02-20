package com.hedvig.backoffice.services.hopeAutocomplete;

import com.hedvig.backoffice.services.hopeAutocomplete.dto.AutocompleteSuggestion;
import com.hedvig.backoffice.services.hopeAutocomplete.dto.SelectSuggestionRequestBodyDto;

import java.util.ArrayList;
import java.util.List;

public class HopeAutocompleteServiceStub implements HopeAutocompleteService {
  @Override
  public List<AutocompleteSuggestion> getAutocompleteSuggestions(String query) {
    final List<AutocompleteSuggestion> list = new ArrayList<>();
    list.add(new AutocompleteSuggestion(0.1337d, "Foo"));
    list.add(new AutocompleteSuggestion(0.42d, "Bar"));
    return list;
  }

  @Override
  public void selectSuggestion(SelectSuggestionRequestBodyDto requestBody) {
    // noop
  }
}
