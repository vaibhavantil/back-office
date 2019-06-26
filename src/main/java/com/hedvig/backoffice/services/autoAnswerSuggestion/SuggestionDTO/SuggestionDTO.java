package com.hedvig.backoffice.services.autoAnswerSuggestion.SuggestionDTO;

import lombok.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;


public class SuggestionDTO {

  String intent;
  String reply;
  String text;
  String allReplies;

  public SuggestionDTO(String autoAnswerServiceReply) {
    //strip [ and ] in beginning and end of string
    String result = autoAnswerServiceReply.substring(1, autoAnswerServiceReply.length() - 2);

    try {
      JSONObject obj = new JSONObject(result);
      this.intent = obj.getString("intent");
      this.reply = obj.getString("reply");
      this.text = obj.getString("text");
      this.allReplies = obj.getString("allReplies");


    } catch (JSONException e) {

      e.printStackTrace();
    }



  }
}
