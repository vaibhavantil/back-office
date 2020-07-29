package com.hedvig.backoffice.services.qualityassurance

import com.hedvig.backoffice.services.members.MemberServiceClient
import com.hedvig.backoffice.services.qualityassurance.dto.UnsignMemberRequest
import org.springframework.stereotype.Service

@Service
class QualityAssuranceServiceImpl(
  private val memberServiceClient: MemberServiceClient
) : QualityAssuranceService {
  override fun unsignMember(request: UnsignMemberRequest, market: String): Boolean {
    memberServiceClient.unsignMember(market, request)
    return true
  }

}
