package com.hedvig.backoffice.services.autoAnswerSuggestion;


import com.hedvig.backoffice.config.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
  name = "autoAnswerSuggestion-service",
  url = "${autoAnswerSuggestionService.baseUrl}",
  configuration = FeignConfig.class)

public interface AutoAnswerSuggestionServiceClient {

  @GetMapping("/v0/answer")
  String getSuggestedAnswer(@RequestParam("text") String question);

  }






