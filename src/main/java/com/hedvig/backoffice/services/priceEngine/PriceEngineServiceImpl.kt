package com.hedvig.backoffice.services.priceEngine

import com.hedvig.backoffice.services.priceEngine.dto.AddNorwegianPostalCodeRequest
import com.hedvig.backoffice.services.priceEngine.dto.CreateNorwegianGripenRequest

class PriceEngineServiceImpl(
  private val priceEngineClient: PriceEngineClient
) : PriceEngineService {
  override fun createNorwegianGripenPriceEngine(request: CreateNorwegianGripenRequest, token: String) {
    priceEngineClient.createNorwegianGripenPriceEngine(request, token)
  }

  override fun addNorwegianPostalCoodes(postalCodesString: String) {
    priceEngineClient.addNorwegianPostalCodes(AddNorwegianPostalCodeRequest(postalCodesString))
  }
}
