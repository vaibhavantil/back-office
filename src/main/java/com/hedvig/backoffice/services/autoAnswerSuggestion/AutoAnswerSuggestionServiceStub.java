package com.hedvig.backoffice.services.autoAnswerSuggestion;

import com.hedvig.backoffice.services.autoAnswerSuggestion.SuggestionDTO.SuggestionDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutoAnswerSuggestionServiceStub implements AutoAnswerSuggestionService {

  @Override
  public SuggestionDTO getAnswerSuggestion(String question) {


    return new SuggestionDTO("Test answer");
  }
}
