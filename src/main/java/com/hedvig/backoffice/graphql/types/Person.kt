package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.members.dto.DebtDTO
import com.hedvig.backoffice.services.members.dto.Flag
import com.hedvig.backoffice.services.members.dto.PersonStatusDTO

data class Person(
  val personFlags: Flag,
  val debt: DebtDTO,
  val whitelisted: Whitelisted?,
  val status: PersonStatusDTO
)
