package com.hedvig.backoffice.services.meerkat;

import com.hedvig.backoffice.services.meerkat.dto.SanctionStatus;
import org.apache.commons.lang3.RandomUtils;

public class MeerkatStub implements Meerkat {

  @Override
  public SanctionStatus getMemberSanctionStatus(String fullName) {
    return SanctionStatus.values()[RandomUtils.nextInt(0, 3)];
  }
}
