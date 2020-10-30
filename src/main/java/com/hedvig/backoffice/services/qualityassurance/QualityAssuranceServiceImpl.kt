package com.hedvig.backoffice.services.qualityassurance

import com.hedvig.backoffice.services.members.MemberServiceClient
import com.hedvig.backoffice.services.qualityassurance.dto.UnsignMemberRequest
import org.springframework.stereotype.Service

@Service
class QualityAssuranceServiceImpl(
  private val memberServiceClient: MemberServiceClient
) : QualityAssuranceService {
  override fun unsignMember(request: UnsignMemberRequest): Boolean {
    memberServiceClient.unsignMember(request)
    return true
  }

}
