package com.hedvig.backoffice.web.dto.assets;

import com.hedvig.common.constant.AssetState;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class AssetStateDTO {

    private String id;

    @NotNull
    private AssetState state;
}
