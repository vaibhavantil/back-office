package com.hedvig.backoffice.services.autoAnswerSuggestion.SuggestionDTO;

import lombok.Value;


@Value
public class AutoLabelDTO {
  String message;

  public AutoLabelDTO(String answer) {

    this.message = answer;

  }


}
