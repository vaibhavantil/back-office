package com.hedvig.backoffice.services.underwriter

import com.fasterxml.jackson.databind.JsonNode
import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.underwriter.dtos.ActivateQuoteRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.AddAgreementFromQuoteRequest
import com.hedvig.backoffice.services.underwriter.dtos.QuoteDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteForNewContractRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteFromAgreementRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteInputDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteResponseDto
import com.hedvig.backoffice.services.underwriter.dtos.SignQuoteFromHopeRequestDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

// TODO rename completeQuoteId to quoteId
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

  @PostMapping("/_/v1/quotes/{completeQuoteId}/signFromHope")
  fun signQuoteForNewContract(
    @PathVariable completeQuoteId: UUID,
    @RequestBody request: SignQuoteFromHopeRequestDto
  ): ResponseEntity<Any>

  @GetMapping("/_/v2/quotes/{completeQuoteId}/schema")
  fun getSchemaFromQuote(@PathVariable completeQuoteId: UUID): ResponseEntity<JsonNode>
}
