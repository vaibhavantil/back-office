package com.hedvig.backoffice.graphql.types;

import com.hedvig.backoffice.services.members.dto.MemberDTO;
import lombok.Value;

@Value
public class Member {
  String memberId;
  String firstName;
  String lastName;
  String personalNumber;
  String address;
  String postalNumber;
  String city;

  public static Member fromDTO(MemberDTO dto) {
    return new Member(dto.getMemberId().toString(), dto.getFirstName(), dto.getLastName(),
        dto.getSsn(), dto.getStreet(), dto.getZipCode(), dto.getCity());
  }
}
