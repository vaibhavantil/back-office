package com.hedvig.backoffice.services.autoAnswerSuggestion;


import com.hedvig.backoffice.config.feign.FeignConfig;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
  name = "autoAnswerSuggestion-service",
  url = "${autoAnswerSuggestionService.baseUrl}",
  configuration = FeignConfig.class)

public interface AutoAnswerSuggestionServiceClient {

  @GetMapping("/v0/answer")
  String getSuggestedAnswer(@RequestParam("text") String question);

  @PostMapping("/v0/autoLabel")
  void autoLabelQuestion(@RequestParam("question") String question, @RequestParam("label") String label);

  }






