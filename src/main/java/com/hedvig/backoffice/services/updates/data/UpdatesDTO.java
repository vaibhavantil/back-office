package com.hedvig.backoffice.services.updates.data;

import com.hedvig.backoffice.domain.Updates;
import com.hedvig.backoffice.services.updates.UpdateType;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class UpdatesDTO {

  private UpdateType type;
  private long count;

  public static UpdatesDTO fromDomain(Updates updates) {
    return new UpdatesDTO(updates.getType(), updates.getCount());
  }
}
