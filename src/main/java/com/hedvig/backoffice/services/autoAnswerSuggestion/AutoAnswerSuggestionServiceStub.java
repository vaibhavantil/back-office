package com.hedvig.backoffice.services.autoAnswerSuggestion;

import com.hedvig.backoffice.services.autoAnswerSuggestion.SuggestionDTO.SuggestionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class AutoAnswerSuggestionServiceStub implements AutoAnswerSuggestionService {

  @Override
  public SuggestionDTO getAnswerSuggestion(String question) {

    String sampleAnswer = "[\n" +
      "    {\n" +
      "        \"allReplies\": {\n" +
      "            \"bye\": {\n" +
      "                \"reply\": \"Hejdå! \uD83D\uDC4B\\n\"\n" +
      "            },\n" +
      "            \"do_you_offer_insurance_x\": {\n" +
      "                \"reply\": \"I dagsläget så erbjuder vi enbart hemförsäkring för hyres- och bostadsrätter, samt objektförsäkring för dyrare prylar. Vi hoppas att kunna erbjuda fler försäkringar i framtiden! ⭐\\n\"\n" +
      "            },\n" +
      "            \"hello\": {\n" +
      "                \"fallback_to\": \"other\",\n" +
      "                \"reply\": \"Hej du! \uD83D\uDE03\\n\",\n" +
      "                \"requires\": [\n" +
      "                    \"hej\",\n" +
      "                    \"heej\",\n" +
      "                    \"hejsan\",\n" +
      "                    \"tja\",\n" +
      "                    \"tjena\",\n" +
      "                    \"hallå\",\n" +
      "                    \"halloj\"\n" +
      "                ]\n" +
      "            },\n" +
      "            \"insurance_start_date\": {\n" +
      "                \"reply\": \"Om du har klickat i att du redan har en befintlig försäkring så kommer din försäkring att aktiveras när vi får information om när din nuvarande försäkrings bindningstid är slut. Du kommer att informeras om när detta sker via mail och du kan även se i appen när din försäkring är aktiv. Om du har valt att du inte har ett befintligt försäkringsbolag kommer din försäkring aktiveras så fort du skriver på avtalet med bankID \uD83D\uDE0A\\n\"\n" +
      "            },\n" +
      "            \"is_x_included\": {\n" +
      "                \"reply\": \"I vår hemförsäkring ingår ansvarsskydd, reseskydd, boendeskydd, personskydd och drulle (allriskförsäkring). \uD83E\uDD29\\n\"\n" +
      "            },\n" +
      "            \"next_fee_date\": {\n" +
      "                \"reply\": \"Pengarna dras från ditt bankkonto via autogiro den 27e varje månad \uD83D\uDCB8\\n\"\n" +
      "            },\n" +
      "            \"other\": {\n" +
      "                \"reply\": \"\"\n" +
      "            },\n" +
      "            \"payment_info\": {\n" +
      "                \"reply\": \"Ditt pris kan du se om du trycker på “profil” och sedan går till “betalning” \uD83D\uDE0E\\n\"\n" +
      "            }\n" +
      "        },\n" +
      "        \"intent\": \"hello\",\n" +
      "        \"reply\": \"Hej du! \uD83D\uDE03\",\n" +
      "        \"text\": \"hej\"\n" +
      "    }\n" +
      "]";

    return new SuggestionDTO(sampleAnswer);
  }

  @Override
  public void autoLabelQuestion(String question, String label, String memberId,  List<String> messageId) {
    System.out.println(question);
    System.out.println(label);
    System.out.println(memberId);
    System.out.println(messageId);

  }

}

/*
the below is the string returned from python API

[
  {
  "intent": "next_fee_date",
  "reply": "Pengarna dras fr\u00e5n ditt bankkonto via autogiro den 27e varje m\u00e5nad \ud83d\udcb8",
  "text": "N\u00e4r ska n\u00e4sta faktura betalas?"
  "allReplies": {}
  }
  ]
*/
