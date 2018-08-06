package com.hedvig.backoffice.web.dto;

import com.hedvig.backoffice.domain.Personnel;
import lombok.Value;

@Value
public class PersonnelDTO {

  private String id;

  private String email;

  private String name;

  private String picture;

  public static PersonnelDTO fromDomain(Personnel personnel) {
    return new PersonnelDTO(
        personnel.getId(), personnel.getEmail(), personnel.getName(), personnel.getPicture());
  }
}
