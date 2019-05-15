package com.hedvig.backoffice.services.messages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileUploadDTO {
  String fileUploadKey;
  Instant timestamp;
  String mimeType;
  String memberId;
}
