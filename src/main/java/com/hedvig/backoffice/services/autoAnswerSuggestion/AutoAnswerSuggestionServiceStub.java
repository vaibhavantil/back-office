package com.hedvig.backoffice.services.autoAnswerSuggestion;

import com.hedvig.backoffice.services.autoAnswerSuggestion.SuggestionDTO.SuggestionDTO;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AutoAnswerSuggestionServiceStub implements AutoAnswerSuggestionService {

  @Override
  public SuggestionDTO getAnswerSuggestion(String question) {


    return new SuggestionDTO("[{\"intent\": \"sample intent\", \"reply\": \"Sample reply\", \"text\": \"sample question\"}]");
  }

  @Override
  public void autoLabelQuestion(String question, String label) {
    System.out.println(question);
    System.out.println(label);

  }

}

/*
the below is the string returned from python API

[
  {
  "intent": "next_fee_date",
  "reply": "Pengarna dras fr\u00e5n ditt bankkonto via autogiro den 27e varje m\u00e5nad \ud83d\udcb8",
  "text": "N\u00e4r ska n\u00e4sta faktura betalas?"
  }
  ]
*/
