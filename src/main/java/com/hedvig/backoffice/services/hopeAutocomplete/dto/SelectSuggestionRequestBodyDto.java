package com.hedvig.backoffice.services.hopeAutocomplete.dto;

import lombok.Data;

import java.util.List;

@Data
public class SelectSuggestionRequestBodyDto {
  public final String autocompleteQuery;
  public final List<AutocompleteSuggestion> autocompleteResponse;
  public final List<ChatHistoryMessage> chatHistory;
  public final ChatHistoryMessage submittedResponse;
}
