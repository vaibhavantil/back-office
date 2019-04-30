package com.hedvig.backoffice.graphql.types;

import com.hedvig.backoffice.services.messages.dto.FileUploadDTO;
import lombok.Value;

import java.time.Instant;

@Value
public class FileUpload {
  String memberId;
  String fileUploadKey;
  String mimeType;
  Instant timestamp;

  public static FileUpload from(FileUploadDTO file) {
    return new FileUpload(
      file.getMemberId(),
      file.getFileUploadKey(),
      file.getMimeType(),
      file.getTimestamp()
    );
  }
}

