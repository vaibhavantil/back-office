package com.hedvig.backoffice.services.autoAnswerSuggestion;

import com.hedvig.backoffice.config.feign.ExternalServiceNotFoundException;
import com.hedvig.backoffice.services.autoAnswerSuggestion.SuggestionDTO.SuggestionDTO;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AutoAnswerSuggestionServiceImpl implements AutoAnswerSuggestionService {

  private final AutoAnswerSuggestionServiceClient autoAnswerSuggestionServiceClient;

  public AutoAnswerSuggestionServiceImpl(AutoAnswerSuggestionServiceClient autoAnswerSuggestionServiceClient) {
    this.autoAnswerSuggestionServiceClient = autoAnswerSuggestionServiceClient;
  }


  @Override
  public void autoLabelQuestion(String question, String label){

  }


  @Override
  public SuggestionDTO getAnswerSuggestion(String question) {
    SuggestionDTO answer = new SuggestionDTO(autoAnswerSuggestionServiceClient.getSuggestedAnswer(question));

    return answer;

    }

  }


