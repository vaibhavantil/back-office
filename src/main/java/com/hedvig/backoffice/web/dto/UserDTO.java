package com.hedvig.backoffice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserDTO {

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

    public UserDTO(long memberId, String name) {
        this.memberId = memberId;
        this.firstName = name;
    }

    public String getHid() {
        return Long.toString(memberId);
    }

}
