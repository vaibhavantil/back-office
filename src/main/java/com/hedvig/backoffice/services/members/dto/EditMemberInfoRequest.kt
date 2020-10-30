package com.hedvig.backoffice.services.members.dto

data class EditMemberInfoRequest(
  val memberId: String,
  val firstName: String?,
  val lastName: String?,
  val email: String?,
  val phoneNumber: String?
)
