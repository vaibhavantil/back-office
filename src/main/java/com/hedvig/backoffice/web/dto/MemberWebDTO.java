package com.hedvig.backoffice.web.dto;

import com.hedvig.backoffice.services.members.dto.MemberDTO;
import com.hedvig.backoffice.util.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.hedvig.backoffice.util.TzHelper.SWEDEN_TZ;
import static com.hedvig.backoffice.util.TzHelper.toInstant;
import static com.hedvig.backoffice.util.TzHelper.toLocalDateTime;

@Data
@NoArgsConstructor
public class MemberWebDTO {
  Long memberId;
  MemberStatus status;
  String ssn;
  Gender gender;
  String firstName;
  String lastName;
  String street;
  Integer floor;
  String apartment;
  String city;
  String zipCode;
  String country;
  String email;
  String phoneNumber;
  LocalDate birthDate;
  LocalDateTime signedOn;
  LocalDateTime createdOn;
  String fraudulentStatus;
  String fraudulentDescription;
  List<TraceInfoDTO> traceMemberInfo;

  public MemberWebDTO(MemberDTO m) {
    this.memberId = m.getMemberId();
    this.status = m.getStatus();
    this.ssn = m.getSsn();
    this.gender = m.getGender();
    this.firstName = m.getFirstName();
    this.lastName = m.getLastName();
    this.street = m.getStreet();
    this.floor = m.getFloor();
    this.apartment = m.getApartment();
    this.city = m.getCity();
    this.zipCode = m.getZipCode();
    this.country = m.getCountry();
    this.email = m.getEmail();
    this.phoneNumber = m.getPhoneNumber();
    this.birthDate = m.getBirthDate();
    this.signedOn = toLocalDateTime(m.getSignedOn(), SWEDEN_TZ);
    this.createdOn = toLocalDateTime(m.getSignedOn(), SWEDEN_TZ);
    this.fraudulentStatus = m.getFraudulentStatus();
    this.fraudulentDescription = m.getFraudulentDescription();
    this.traceMemberInfo = m .getTraceMemberInfo();
  }

  public MemberDTO convertToMemberDTO() {
    return new MemberDTO(
      this.memberId,
      this.status,
      this.ssn,
      this.gender,
      this.firstName,
      this.lastName,
      this.street,
      this.floor,
      this.apartment,
      this.city,
      this.zipCode,
      this.country,
      this.email,
      this.phoneNumber,
      this.birthDate,
      toInstant(this.signedOn, SWEDEN_TZ),
      toInstant(this.createdOn, SWEDEN_TZ),
      this.fraudulentStatus,
      this.fraudulentDescription,
      this.traceMemberInfo
    );
  }
}
