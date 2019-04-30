package com.hedvig.backoffice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
public class FileUpload {
  @Id private String fileUploadKey;
  private String memberId;
  private Instant timeStamp;
}
