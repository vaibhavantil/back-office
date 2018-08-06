package com.hedvig.backoffice.web.dto.assets;

import com.hedvig.common.constant.AssetState;
import javax.validation.constraints.NotNull;
import lombok.Value;

@Value
public class AssetStateDTO {

  private String id;

  @NotNull private AssetState state;
}
