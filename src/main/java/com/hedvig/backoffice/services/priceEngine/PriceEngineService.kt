package com.hedvig.backoffice.services.priceEngine

import com.hedvig.backoffice.services.priceEngine.dto.CreateNorwegianGripenRequest

interface PriceEngineService {
  fun createNorwegianGripenPriceEngine(request: CreateNorwegianGripenRequest, token: String)
  fun addNorwegianPostalCoodes(postalCodesString: String)
}
