package com.hedvig.backoffice.services.members

import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.members.dto.*
import com.hedvig.backoffice.web.dto.MemberFraudulentStatusDTO
import com.hedvig.backoffice.web.dto.MemberStatus
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*

@FeignClient(name = "member-service", url = "\${memberservice.baseUrl}", configuration = [FeignConfig::class])
interface MemberServiceClient {

  @GetMapping("/i/member/search?status={status}&query={query}")
  fun search(
    @PathVariable("status") status: MemberStatus,
    @PathVariable("query") query: String,
    @RequestHeader("Authorization") token: String
  ): List<MemberDTO>

  @GetMapping("/i/member/searchPaged")
  fun searchPaged(
    @RequestParam("status") status: MemberStatus?,
    @RequestParam("query") query: String,
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
  fun getPerson(@PathVariable("memberId") memberId: String): PersonDTO

  @PostMapping("/_/person/member/whitelist/{memberId}")
  fun whitelistMember(
    @PathVariable("memberId") memberId: String,
    @RequestParam("whitelistedBy") whitelistedBy: String
  )
}
