package com.hedvig.backoffice.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hedvig.backoffice.domain.Personnel;
import lombok.Value;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

@Value
public class PersonnelDTO {

    private Long id;

    @NotNull
    @Email
    private String email;

    @JsonIgnore
    private String password;

    public static PersonnelDTO fromDomain(Personnel personnel) {
        return new PersonnelDTO(personnel.getId(), personnel.getEmail(),  "");
    }

    public static Personnel toDomain(PersonnelDTO dto) {
        return new Personnel(dto.getEmail(), dto.getPassword());
    }

}
