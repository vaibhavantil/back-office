package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.members.dto.MemberDTO
import java.time.Instant

data class Member(
  val memberId: String,
  val firstName: String?,
  val lastName: String?,
  val personalNumber: String?,
  val address: String?,
  val postalNumber: String?,
  val city: String?,
  val signedOn: Instant?,
  val fraudulentStatus: String?,
  val fraudulentStatusDescription: String?
) {
  companion object {
    fun fromDTO(dto: MemberDTO): Member = Member(
      dto.memberId.toString(),
      dto.firstName,
      dto.lastName,
      dto.ssn,
      dto.street,
      dto.zipCode,
      dto.city,
      dto.signedOn,
      dto.fraudulentStatus,
      dto.fraudulentDescription
    )
  }
}
