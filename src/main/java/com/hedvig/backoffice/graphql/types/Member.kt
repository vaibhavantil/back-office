package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.members.dto.MemberDTO
import com.hedvig.backoffice.util.Gender
import java.time.Instant
import java.time.LocalDate

data class Member(
  val memberId: String,
  val email: String?,
  val phoneNumber: String?,
  val firstName: String?,
  val lastName: String?,
  val personalNumber: String?,
  val birthDate: LocalDate?,
  val gender: Gender?,
  val fraudulentStatus: String?,
  val fraudulentStatusDescription: String?,
  val createdOn: Instant?,
  val signedOn: Instant?,
  val status: String?
) {
  companion object {
    fun fromDTO(dto: MemberDTO): Member = Member(
      memberId = dto.memberId.toString(),
      email = dto.email,
      phoneNumber = dto.phoneNumber,
      firstName = dto.firstName,
      lastName = dto.lastName,
      personalNumber = dto.ssn,
      birthDate = dto.birthDate,
      gender = dto.gender,
      fraudulentStatus = dto.fraudulentStatus,
      fraudulentStatusDescription = dto.fraudulentDescription,
      createdOn = dto.createdOn,
      signedOn = dto.signedOn,
      status = dto.status?.name
    )
  }
}
