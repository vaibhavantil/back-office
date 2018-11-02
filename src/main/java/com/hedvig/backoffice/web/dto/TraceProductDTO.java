package com.hedvig.backoffice.web.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TraceProductDTO {
  private Long id;
  private LocalDateTime date;
  private String oldValue;
  private String newValue;
  private String fieldName;
  private UUID productId;
  private String userId;
  private Boolean isSuccess;
}
