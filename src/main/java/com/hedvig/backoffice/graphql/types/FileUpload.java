package com.hedvig.backoffice.graphql.types;

import lombok.Value;
import java.net.URL;
import java.time.Instant;

@Value
public class FileUpload {
  URL fileUploadUrl;
  Instant timestamp;
  String mimeType;
  String memberId;
}

