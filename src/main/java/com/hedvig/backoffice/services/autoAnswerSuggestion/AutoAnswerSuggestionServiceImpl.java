package com.hedvig.backoffice.services.autoAnswerSuggestion;

import com.hedvig.backoffice.services.autoAnswerSuggestion.DTOs.SuggestionDTO;
import java.util.List;


public class AutoAnswerSuggestionServiceImpl implements AutoAnswerSuggestionService {

  private final AutoAnswerSuggestionServiceClient autoAnswerSuggestionServiceClient;

  public AutoAnswerSuggestionServiceImpl(AutoAnswerSuggestionServiceClient autoAnswerSuggestionServiceClient) {
    this.autoAnswerSuggestionServiceClient = autoAnswerSuggestionServiceClient;
  }


  @Override
  public void autoLabelQuestion(String question, String label, String memberId,  List<String> messageIds){
    autoAnswerSuggestionServiceClient.autoLabelQuestion(question, label, memberId, messageIds);

  }

  @Override
  public List<SuggestionDTO> getAnswerSuggestion(String question) {
    return autoAnswerSuggestionServiceClient.getSuggestedAnswer(question);

    }

  }

