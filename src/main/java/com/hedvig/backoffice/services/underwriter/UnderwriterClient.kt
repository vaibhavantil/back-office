package com.hedvig.backoffice.services.underwriter

import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.underwriter.dtos.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@FeignClient(
  name = "underwriter",
  url = "\${underwriter.baseUrl:underwriter}",
  configuration = [FeignConfig::class]
)
interface UnderwriterClient {
  @PostMapping("/_/v1/quotes")
  fun createQuote(@RequestBody quote: QuoteRequestDto): QuoteResponseDto

  @PatchMapping("/_/v1/quotes/{quoteId}")
  fun updateQuote(
    @PathVariable("quoteId") quoteId: UUID,
    @RequestBody quoteDto: QuoteInputDto,
    @RequestParam("underwritingGuidelinesBypassedBy") underwritingGuidelinesBypassedBy: String?
  ): QuoteDto

  @PostMapping("/_/v1/quotes/{quoteId}/complete")
  fun completeQuote(
    @PathVariable("quoteId") quoteId: UUID,
    @RequestParam("underwritingGuidelinesBypassedBy") underwritingGuidelinesBypassedBy: String?
  )

  @PostMapping("/_/v1/quotes/{quoteId}/activate")
  fun activateQuote(@PathVariable("quoteId") quoteId: UUID, @RequestBody body: ActivateQuoteRequestDto): QuoteDto

  @PostMapping("/_/v1/quotes/add/agreement")
  fun addAgreementFromQuote(@RequestBody request: AddAgreementFromQuoteRequest): QuoteDto

  @GetMapping("/_/v1/quotes/members/{memberId}")
  fun getQuotes(@PathVariable("memberId") memberId: String): List<QuoteDto>

  @GetMapping("/_/v1/quotes/{quoteId}")
  fun getQuote(@PathVariable("quoteId") quoteId: UUID): QuoteDto

  @PostMapping("/_/v1/quotes/createQuoteFromAgreement")
  fun createQuoteFromAgreement(
    @RequestBody quoteRequest: QuoteFromAgreementRequestDto
  ): QuoteResponseDto

  @PostMapping("/_/v1/quotes/createQuoteForNewContract")
  fun createQuoteForNewContract(
    @RequestBody quoteRequest: QuoteForNewContractRequestDto
  ): QuoteResponseDto
}
