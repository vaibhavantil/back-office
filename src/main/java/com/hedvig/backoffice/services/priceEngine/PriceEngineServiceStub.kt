package com.hedvig.backoffice.services.priceEngine

import com.hedvig.backoffice.services.priceEngine.dto.CreateNorwegianGripenRequest

class PriceEngineServiceStub: PriceEngineService {
  override fun createNorwegianGripenPriceEngine(request: CreateNorwegianGripenRequest, token: String) {
    // noop
  }

  override fun addNorwegianPostalCoodes(postalCodesString: String) {
    // noop
  }
}
