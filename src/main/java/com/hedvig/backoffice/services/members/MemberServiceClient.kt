package com.hedvig.backoffice.services.members

import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.members.dto.*
import com.hedvig.backoffice.services.qualityassurance.dto.UnsignMemberRequest
import com.hedvig.backoffice.web.dto.MemberFraudulentStatusDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
  fun findPickedLocaleByMemberId(@PathVariable("memberId") memberId: String): PickedLocaleDTO

  @PostMapping("/_/staging/{market}/unsignMember")
  fun unsignMember(
    @PathVariable("market") market: String,
    @RequestBody request: UnsignMemberRequest
  )

  @PostMapping("/_/member/info/edit")
  fun editMemberInfo(
    @PathVariable request: EditMemberInfoRequest,
    @RequestHeader("Authorization") token: String
  )
}
