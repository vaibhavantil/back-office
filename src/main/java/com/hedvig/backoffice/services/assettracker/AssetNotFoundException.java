package com.hedvig.backoffice.services.assettracker;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AssetNotFoundException extends Exception {

  public AssetNotFoundException(String message) {
    super(message);
  }
}
