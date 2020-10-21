package com.hedvig.backoffice.services.account

import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.account.dto.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*

@FeignClient(
  name = "account-service",
  url = "\${accountService.baseUrl:account-service}",
  configuration = [FeignConfig::class]
)
interface AccountServiceClient {
  @PostMapping("/_/accounts/{memberId}/entries")
  fun addAccountEntry(
    @PathVariable("memberId") memberId: String,
    @RequestBody requestBody: AccountEntryRequestDTO
  )

  @GetMapping("/_/accounts/{memberId}")
  fun getAccount(@PathVariable("memberId") memberId: String): AccountDTO

  @PostMapping("/_/accounts/batchFind")
  fun batchFindCurrentBalances(@RequestBody memberIds: List<String>): List<AccountDTO?>

  @GetMapping("/_/schedule/states")
  fun getSubscriptionsPendingApproval(
    @RequestParam("status") status: ChargeStatus
  ): List<SchedulerStateDto>

  @PostMapping("/_/schedule/approvals")
  fun addApprovedSubscriptions(
    @RequestBody requestBody: List<ApproveChargeRequestDto>,
    @RequestParam("approvedBy") approvedBy: String
  )

  @GetMapping("/_/numberFailedCharges/{memberId}")
  fun getNumberFailedCharges(@PathVariable memberId: String): NumberFailedChargesDto

  @PostMapping("/_/schedule/subscription/backfill/{memberId}")
  fun backfillSubscriptions(
    @PathVariable memberId: String,
    @RequestParam backfilledBy: String
  )
}
