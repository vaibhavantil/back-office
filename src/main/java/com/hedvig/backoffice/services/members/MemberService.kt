package com.hedvig.backoffice.services.members

import com.hedvig.backoffice.services.members.dto.*
import com.hedvig.backoffice.web.dto.MemberFraudulentStatusDTO
import com.hedvig.backoffice.web.dto.MemberStatus

import org.springframework.data.domain.Sort


interface MemberService {

    fun search(status: MemberStatus, query: String, token: String): List<MemberDTO>

    fun searchPaged(
      status: MemberStatus?,
      query: String,
      page: Int?,
      pageSize: Int?,
      sortBy: MembersSortColumn,
      sortDirection: Sort.Direction,
      token: String
    ): MembersSearchResultDTO

    fun findByMemberId(memberId: String, token: String): MemberDTO

    fun editMember(memberId: String, memberDTO: MemberDTO, token: String)

    fun cancelInsurance(hid: String, dto: InsuranceCancellationDTO, token: String)

    fun getMembersByIds(ids: List<String>): List<MemberDTO>

    fun setFraudulentStatus(memberId: String, dto: MemberFraudulentStatusDTO, token: String)

    fun getPerson(memberId: String): PersonDTO

    fun whitelistMember(memberId: String, whitelistedBy: String)
}
