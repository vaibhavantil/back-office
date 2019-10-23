package com.hedvig.backoffice.services.members.dto

import java.time.Instant
import java.time.LocalDate

import com.hedvig.backoffice.web.dto.MemberStatus
import com.hedvig.backoffice.web.dto.TraceInfoDTO

data class MemberDTO(
    val memberId: Long,
    val status: MemberStatus? = null,
    val ssn: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val street: String? = null,
    val floor: Int? = null,
    val apartment: String? = null,
    val city: String? = null,
    val zipCode: String? = null,
    val country: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val birthDate: LocalDate? = null,
    val signedOn: Instant? = null,
    val createdOn: Instant? = null,
    val fraudulentStatus: String? = null,
    val fraudulentDescription: String? = null,
    val traceMemberInfo: List<TraceInfoDTO>? = null
) {
  companion object {
    fun from(memberId: String) = MemberDTO(
      memberId.toLong()
    )
  }
}
