package com.hedvig.backoffice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

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

    public MemberDTO(long memberId) {
        this.memberId = memberId;
    }

    public String getHid() {
        return Long.toString(memberId);
    }

}
