package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.assettracker.AssetNotFoundException;
import com.hedvig.backoffice.services.assettracker.AssetTrackerException;
import com.hedvig.backoffice.services.assettracker.AssetTrackerService;
import com.hedvig.backoffice.web.dto.assets.AssetDTO;
import com.hedvig.backoffice.web.dto.assets.AssetStateDTO;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

  private final AssetTrackerService assetTrackerService;

  @Autowired
  public AssetController(AssetTrackerService assetTrackerService) {
    this.assetTrackerService = assetTrackerService;
  }

  @GetMapping
  public List<AssetDTO> findAll() {
    return assetTrackerService.findAll();
  }

  @GetMapping("/{assetId}")
  public AssetDTO find(@PathVariable("assetId") String assetId) throws AssetNotFoundException {
    return assetTrackerService.find(assetId);
  }

  @PostMapping(value = "/{assetId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AssetStateDTO> update(
      @PathVariable("assetId") String assetId, @RequestBody @Valid AssetStateDTO state)
      throws AssetNotFoundException, AssetTrackerException {

    assetTrackerService.changeAssetState(assetId, state.getState());
    return new ResponseEntity<>(new AssetStateDTO(assetId, state.getState()), HttpStatus.OK);
  }
}
