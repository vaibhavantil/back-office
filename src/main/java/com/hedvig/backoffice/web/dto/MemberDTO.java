package com.hedvig.backoffice.web.dto;

import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDTO {

  private Long memberId;

  private String status;

  private String ssn;

  private String firstName;

  private String lastName;

  private String street;

  private Integer floor;

  private String apartment;

  private String city;

  private String zipCode;

  private String country;

  private String email;

  private String phoneNumber;

  private LocalDate birthDate;

  private Instant signedOn;

  private Instant createdOn;

  public MemberDTO(long memberId) {
    this.memberId = memberId;
  }
}
