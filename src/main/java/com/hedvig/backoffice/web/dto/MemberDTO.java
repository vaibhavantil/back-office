package com.hedvig.backoffice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MemberDTO {

    private Long memberId;
    private String ssn;

    private String firstName;
    private String lastName;

    private String street;
    private String city;
    private String zipCode;

    private String email;
    private String phoneNumber;
    private String country;

    private LocalDate birthDate;
    private String apartment;

    public MemberDTO(long memberId) {
        this.memberId = memberId;
    }

    public String getHid() {
        return Long.toString(memberId);
    }

}
