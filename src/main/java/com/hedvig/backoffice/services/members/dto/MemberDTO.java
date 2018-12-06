package com.hedvig.backoffice.services.members.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import com.hedvig.backoffice.web.dto.MemberStatus;
import com.hedvig.backoffice.web.dto.TraceInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDTO {

  private Long memberId;

  private MemberStatus status;

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

  private String fraudulentStatus;

  private String fraudulentDescription;

  private List<TraceInfoDTO> traceMemberInfo;

  public MemberDTO(long memberId) {
    this.memberId = memberId;
  }
}
