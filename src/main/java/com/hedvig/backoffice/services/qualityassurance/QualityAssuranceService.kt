package com.hedvig.backoffice.services.qualityassurance

import com.hedvig.backoffice.services.qualityassurance.dto.UnsignMemberRequest

interface QualityAssuranceService {
  fun unsignMember(request: UnsignMemberRequest): Boolean
}
