package com.hedvig.backoffice.services.assettracker;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class AssetTrackerException extends Exception {

  public AssetTrackerException(String message) {
    super(message);
  }

  public AssetTrackerException(Throwable e) {
    super(e);
  }
}
