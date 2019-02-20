package com.hedvig.backoffice.services.hopeAutocomplete;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.hopeAutocomplete.dto.AutocompleteSuggestion;
import com.hedvig.backoffice.services.hopeAutocomplete.dto.RemoteAutocompleteSuggestionRequestBodyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "hope-autocomplete", url = "${hopeAutocompleteService.baseUrl:hope-autocomplete-service}", configuration = FeignConfig.class)
public interface HopeAutocompleteClient {
  @GetMapping("/v0/messages/autocomplete?query={query}")
  List<AutocompleteSuggestion> getAutocomplete(@RequestParam("query") String query);

  @PostMapping("/v0/messages/autocomplete")
  void selectAutocompleteSuggestion(@RequestBody RemoteAutocompleteSuggestionRequestBodyDto requestBody);
}
