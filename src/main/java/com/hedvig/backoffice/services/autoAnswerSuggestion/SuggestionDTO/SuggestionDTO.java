package com.hedvig.backoffice.services.autoAnswerSuggestion.SuggestionDTO;

import lombok.Value;


@Value
public class SuggestionDTO {
  String message;

  public SuggestionDTO(String answer) {

    this.message = answer;

  }


}
