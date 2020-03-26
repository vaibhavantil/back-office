package com.hedvig.backoffice.services.product_pricing.dto.contract

enum class ContractStatus {
  PENDING,
  ACTIVE_IN_FUTURE,
  ACTIVE_IN_FUTURE_AND_TERMINATED_IN_FUTURE,
  ACTIVE,
  TERMINATED_TODAY,
  TERMINATED_IN_FUTURE,
  TERMINATED
}
