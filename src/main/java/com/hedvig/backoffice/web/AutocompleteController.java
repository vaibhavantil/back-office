package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.hopeAutocomplete.HopeAutocompleteService;
import com.hedvig.backoffice.services.hopeAutocomplete.dto.AutocompleteSuggestion;
import com.hedvig.backoffice.services.hopeAutocomplete.dto.SelectSuggestionRequestBodyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.requireNonNull;

@RestController
@RequestMapping("/api/autocomplete")
public class AutocompleteController {
  private final HopeAutocompleteService autocompleteService;

  @Autowired
  public AutocompleteController(final HopeAutocompleteService autocompleteService) {
    this.autocompleteService = requireNonNull(autocompleteService);
  }

  @GetMapping(path = {"/suggestions"})
  public List<AutocompleteSuggestion> getSuggestions(@RequestParam("query") final String query) {
    return autocompleteService.getAutocompleteSuggestions(query);
  }

  @PostMapping(path = {"select"})
  public void selectSuggestion(@RequestBody final SelectSuggestionRequestBodyDto requestBody) {
    autocompleteService.selectSuggestion(requestBody);
  }
}
