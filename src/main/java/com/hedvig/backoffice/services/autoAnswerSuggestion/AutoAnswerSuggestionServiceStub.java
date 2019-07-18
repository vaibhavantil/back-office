package com.hedvig.backoffice.services.autoAnswerSuggestion;

import com.google.common.collect.Lists;
import com.hedvig.backoffice.services.autoAnswerSuggestion.DTOs.ReplyEntryDTO;
import com.hedvig.backoffice.services.autoAnswerSuggestion.DTOs.SuggestionDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class AutoAnswerSuggestionServiceStub implements AutoAnswerSuggestionService {

  private ArrayList<ReplyEntryDTO> allRepliesArray = new ArrayList<>();

  public AutoAnswerSuggestionServiceStub() {

    allRepliesArray.add(new ReplyEntryDTO("insurance_start_date",
      "Om du har klickat i att du redan har en befintlig försäkring så\n" +
        "    kommer din försäkring att aktiveras när vi får information om när\n" +
        "    din nuvarande försäkrings bindningstid är slut. Du kommer att informeras\n" +
        "    om när detta sker via mail och du kan även se i appen när din försäkring\n" +
        "    är aktiv. Om du har valt att du inte har ett befintligt försäkringsbolag\n" +
        "    kommer din försäkring aktiveras så fort du skriver på avtalet med\n" +
        "    bankID \uD83D\uDE0A"));

    allRepliesArray.add(new ReplyEntryDTO("do_you_offer_insurance_x",
      "I dagsläget så erbjuder vi enbart hemförsäkring för hyres- och bostadsrätter, samt objektförsäkring för dyrare prylar. Vi hoppas att kunna erbjuda fler försäkringar i framtiden!"));

    allRepliesArray.add(new ReplyEntryDTO("is_x_included",
      "I vår hemförsäkring ingår ansvarsskydd, reseskydd, boendeskydd,\n" +
        "    personskydd och drulle (allriskförsäkring)."));

    allRepliesArray.add(new ReplyEntryDTO("next_fee_date",
      "Pengarna dras från ditt bankkonto via autogiro den 27e varje månad"));

    allRepliesArray.add(new ReplyEntryDTO("payment_info",
      "Betalningen sker månadsvis via autogiro med hjälp av Trustly. Du kan se om pengarna dragits och om ditt autogiro är aktivt ... Du anmäler dit autogiro ..."));


  }

  @Override
  public List<SuggestionDTO> getAnswerSuggestion(String question) {

    final String intent = "next_fee_date";
    final String reply = "Pengarna dras från ditt bankkonto via autogiro den 27e varje månad";
    final String text = "När dras degen?";
    final Float confidence = 0.784f;


    return Lists.newArrayList(new SuggestionDTO(intent, reply, text, confidence, allRepliesArray));
  }

  @Override
  public void autoLabelQuestion(String question, String label, String memberId,  List<String> messageIds) {
    log.info(question);
    log.info(label);
    log.info(memberId);

  }

}
