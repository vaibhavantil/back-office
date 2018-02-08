package com.hedvig.backoffice.web.dto.claims.fields;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "TEXT", value = ClaimTextFiled.class),
        @JsonSubTypes.Type(name = "DATE", value = ClaimDateField.class),
        @JsonSubTypes.Type(name = "ASSET", value = ClaimAssetField.class),
        @JsonSubTypes.Type(name = "BOOL", value = ClaimBoolField.class)
})
public class ClaimField {
    @NotNull
    private String name;

    @NotNull
    private String title;
}
