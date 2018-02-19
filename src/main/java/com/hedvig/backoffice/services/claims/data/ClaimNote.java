package com.hedvig.backoffice.services.claims.data;

import lombok.Data;

@Data
public class ClaimNote extends ClaimBackOffice {

    private String text;
    private String fileURL;

}
