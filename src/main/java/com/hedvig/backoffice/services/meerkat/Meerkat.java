package com.hedvig.backoffice.services.meerkat;

import com.hedvig.backoffice.services.meerkat.dto.SanctionStatus;

public interface Meerkat {

  SanctionStatus getMemberSanctionStatus(String fullName);
}
