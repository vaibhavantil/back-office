package com.hedvig.backoffice.services.hopeAutocomplete;

import com.hedvig.backoffice.services.hopeAutocomplete.dto.AutocompleteSuggestion;
import com.hedvig.backoffice.services.hopeAutocomplete.dto.RemoteAutocompleteSuggestionRequestBodyDto;
import com.hedvig.backoffice.services.hopeAutocomplete.dto.SelectSuggestionRequestBodyDto;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class HopeAutocompleteServiceImpl implements HopeAutocompleteService {
  private final HopeAutocompleteClient client;

  public HopeAutocompleteServiceImpl(final HopeAutocompleteClient client) {
    this.client = requireNonNull(client);
  }

  @Override
  public List<AutocompleteSuggestion> getAutocompleteSuggestions(final String query) {
    return client.getAutocomplete(query);
  }

  @Override
  public void selectSuggestion(final SelectSuggestionRequestBodyDto requestBody) {
    client.selectAutocompleteSuggestion(RemoteAutocompleteSuggestionRequestBodyDto.from(requestBody));
  }
}
