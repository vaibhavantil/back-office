package com.hedvig.backoffice.services.messages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor

@Data
public class FileUploadDTO {
  UUID id;
  String memberId;
  String fileUploadKey;
  String mimeType;
  Instant timestamp;
}
