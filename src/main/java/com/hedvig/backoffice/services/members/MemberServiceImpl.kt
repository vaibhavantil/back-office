package com.hedvig.backoffice.services.members

import com.hedvig.backoffice.services.members.dto.*
import com.hedvig.backoffice.web.dto.MemberFraudulentStatusDTO
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus

class MemberServiceImpl(private val client: MemberServiceClient) : MemberService {

    init {
        logger.info("MEMBER SERVICE:")
        logger.info("class: " + MemberServiceImpl::class.java.name)
    }

    override fun search(includeAll: Boolean?, query: String, token: String): List<MemberDTO> {
        return client.search(includeAll, query, token)
    }

    override fun searchPaged(
        includeAll: Boolean?,
        query: String?,
        page: Int?,
        pageSize: Int?,
        sortBy: MembersSortColumn,
        sortDirection: Sort.Direction,
        token: String
    ): MembersSearchResultDTO {
        return client.searchPaged(includeAll, query, page, pageSize, sortBy, sortDirection, token)
    }

    override fun findByMemberId(memberId: String, token: String): MemberDTO {
        return client.member(memberId, token)
    }

    override fun editMember(memberId: String, memberDTO: MemberDTO, token: String) {
        if (client.member(memberId, token) != memberDTO)
            client.editMember(memberId, memberDTO, token)
    }

    override fun cancelInsurance(hid: String, dto: InsuranceCancellationDTO, token: String) {
        client.cancelInsurance(hid, dto, token)
    }

    override fun getMembersByIds(ids: List<String>): List<MemberDTO> {
        return client.getMembers(ChargeMembersDTO(ids))
    }

    override fun setFraudulentStatus(memberId: String, dto: MemberFraudulentStatusDTO, token: String) {
        client.setFraudulentStatus(memberId, dto, token)
        logger.info("Change member status for " + memberId + ": " + dto.fraudulentStatus + ", " + dto.fraudulentStatusDescription)
    }

    override fun getPerson(memberId: String): PersonDTO {
        val response = client.getPerson(memberId)
        if (response.statusCode == HttpStatus.NOT_FOUND) {
            throw Exception("No person info found for member=${memberId}")
        }
        return response.body!!
    }

    override fun whitelistMember(memberId: String, whitelistedBy: String) {
        client.whitelistMember(memberId, whitelistedBy)
    }

    override fun getPickedLocaleByMemberId(memberId: String): PickedLocaleDTO =
        client.getPickedLocaleByMemberId(memberId)

    override fun editMemberInfo(request: EditMemberInfoRequest, token: String) {
        client.editMemberInfo(request, token)
    }

    override fun identity(memberId: String) =
        client.getIdentity(memberId)

    companion object {
        private val logger = LoggerFactory.getLogger(MemberServiceImpl::class.java)
    }
}
