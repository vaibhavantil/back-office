package com.hedvig.backoffice.services.autoAnswerSuggestion.SuggestionDTO;

import lombok.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class SuggestionDTO {

  String intent;
  String reply;
  String text;
  ArrayList<ReplyEntry> allRepliesArray;

  public SuggestionDTO(String autoAnswerServiceReply) {

    //strip [ and ] in beginning and end of string
    String result = autoAnswerServiceReply.substring(1, autoAnswerServiceReply.length() - 2);

    allRepliesArray = new ArrayList<>();

    try {
      JSONObject obj = new JSONObject(result);
      this.intent = obj.getString("intent");
      this.reply = obj.getString("reply");
      this.text = obj.getString("text");

      JSONObject allReplies = obj.getJSONObject("allReplies");
      Iterator<String> keys = allReplies.keys();

      while (keys.hasNext()) {
        String key = keys.next();
        if (allReplies.get(key) instanceof JSONObject) {

          JSONObject replyObj = allReplies.getJSONObject(key);
          allRepliesArray.add(new ReplyEntry(key, replyObj.getString("reply")));
        }
      }

    } catch (JSONException e) {

      e.printStackTrace();
    }

  }

  public class ReplyEntry {
    String intent;
    String reply;

    public ReplyEntry(String intent, String reply) {
      this.intent = intent;
      this.reply = reply;
    }
  }
}
