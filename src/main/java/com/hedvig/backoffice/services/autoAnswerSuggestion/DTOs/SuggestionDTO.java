package com.hedvig.backoffice.services.autoAnswerSuggestion.DTOs;

import lombok.Value;

import java.util.ArrayList;

@Value
public class SuggestionDTO {

  String intent;
  String reply;
  String text;
  Double confidence;
  ArrayList<ReplyEntryDTO> allReplies;


}
