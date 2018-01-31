package com.hedvig.backoffice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NoteDTO {

    private String text;
    private Long adminId;
    private LocalDateTime date;
    private String fileUrl;

}
