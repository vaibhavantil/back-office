package com.hedvig.backoffice.services.autoAnswerSuggestion;


import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.autoAnswerSuggestion.SuggestionDTO.SuggestionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient(
  name = "autoAnswerSuggestion-service",
  url = "${autoAnswerSuggestionService.baseUrl}",
  configuration = FeignConfig.class)

public interface AutoAnswerSuggestionServiceClient {

  @GetMapping("/v0/answer?text=hej")
  String getSuggestedAnswer(String question);

  }






