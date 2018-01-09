package com.hedvig.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@NoArgsConstructor
@ToString(exclude = "password")
public class Personnel {
    @Id
    @GeneratedValue
    private Long id;

    @Email
    @Column(length = 100, unique = true)
    @NotNull
    @Size(min = 5, max = 100)
    private String email;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(length = 60)
    private String password;

    public Personnel(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
