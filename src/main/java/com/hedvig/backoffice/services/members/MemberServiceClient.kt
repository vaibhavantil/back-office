package com.hedvig.backoffice.services.members

import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.members.dto.ChargeMembersDTO
import com.hedvig.backoffice.services.members.dto.EditMemberInfoRequest
import com.hedvig.backoffice.services.members.dto.IdentityDto
import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO
import com.hedvig.backoffice.services.members.dto.MemberDTO
import com.hedvig.backoffice.services.members.dto.MembersSearchResultDTO
import com.hedvig.backoffice.services.members.dto.MembersSortColumn
import com.hedvig.backoffice.services.members.dto.PersonDTO
import com.hedvig.backoffice.services.members.dto.PickedLocaleDTO
import com.hedvig.backoffice.services.qualityassurance.dto.UnsignMemberRequest
import com.hedvig.backoffice.web.dto.MemberFraudulentStatusDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "member-service", url = "\${memberservice.baseUrl}", configuration = [FeignConfig::class])
interface MemberServiceClient {

    @GetMapping("/i/member/search")
    fun search(
        @RequestParam("includeAll") includeAll: Boolean?,
        @RequestParam("query") query: String,
        @RequestHeader("Authorization") token: String
    ): List<MemberDTO>

    @GetMapping("/i/member/searchPaged")
    fun searchPaged(
        @RequestParam("includeAll") includeAll: Boolean?,
        @RequestParam("query") query: String?,
        @RequestParam("page") page: Int?,
        @RequestParam("pageSize") pageSize: Int?,
        @RequestParam("sortBy") sortBy: MembersSortColumn,
        @RequestParam("sortDirection") sortDirection: Sort.Direction,
        @RequestHeader("Authorization") token: String
    ): MembersSearchResultDTO

    @GetMapping("/i/member/{memberId}")
    fun member(
        @PathVariable("memberId") memberId: String,
        @RequestHeader("Authorization") token: String
    ): MemberDTO

    @PostMapping("/i/member/{memberId}/edit")
    fun editMember(
        @PathVariable("memberId") memberId: String,
        @RequestBody dto: MemberDTO,
        @RequestHeader("Authorization") token: String
    )

    @PostMapping("/i/member/{memberId}/memberCancelInsurance")
    fun cancelInsurance(
        @PathVariable("memberId") memberId: String,
        @RequestBody dto: InsuranceCancellationDTO,
        @RequestHeader("Authorization") token: String
    )

    @PostMapping("/i/member/{memberId}/setFraudulentStatus")
    fun setFraudulentStatus(
        @PathVariable("memberId") memberId: String,
        @RequestBody dto: MemberFraudulentStatusDTO,
        @RequestHeader("Authorization") token: String
    )

    @PostMapping("/i/member/many")
    fun getMembers(@RequestBody memberIds: ChargeMembersDTO): List<MemberDTO>

    @GetMapping("/_/person/member/{memberId}")
    fun getPerson(@PathVariable("memberId") memberId: String): ResponseEntity<PersonDTO>

    @PostMapping("/_/person/member/whitelist/{memberId}")
    fun whitelistMember(
        @PathVariable("memberId") memberId: String,
        @RequestParam("whitelistedBy") whitelistedBy: String
    )

    @GetMapping("/_/member/{memberId}/pickedLocale")
    fun getPickedLocaleByMemberId(@PathVariable("memberId") memberId: String): PickedLocaleDTO

    @PostMapping("/_/staging/unsignMember")
    fun unsignMember(
        @RequestBody request: UnsignMemberRequest
    )

    @PostMapping("/_/member/edit/info")
    fun editMemberInfo(
        @RequestBody request: EditMemberInfoRequest,
        @RequestHeader("Authorization") token: String
    )

    @GetMapping("/_/member/{memberId}/identity")
    fun getIdentity(@PathVariable("memberId") memberId: String): IdentityDto
}
