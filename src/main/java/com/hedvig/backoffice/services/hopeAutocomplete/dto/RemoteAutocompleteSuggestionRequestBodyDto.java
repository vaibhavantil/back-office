package com.hedvig.backoffice.services.hopeAutocomplete.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RemoteAutocompleteSuggestionRequestBodyDto {
  public final String autocompleteQuery;
  public final List<AutocompleteSuggestion> autocompleteResponse;
  public final List<RemoteChatHistoryMessage> chatHistory;
  public final RemoteChatHistoryMessage submittedResponse;

  public static RemoteAutocompleteSuggestionRequestBodyDto from(final SelectSuggestionRequestBodyDto requestBody) {
    return new RemoteAutocompleteSuggestionRequestBodyDto(
      requestBody.autocompleteQuery,
      requestBody.autocompleteResponse,
      RemoteChatHistoryMessage.from(requestBody.chatHistory),
      RemoteChatHistoryMessage.from(requestBody.submittedResponse)
    );
  }
}
