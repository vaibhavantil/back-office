package com.hedvig.backoffice.services.underwriter

import com.fasterxml.jackson.databind.JsonNode
import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.underwriter.dtos.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@FeignClient(
    name = "underwriter",
    url = "\${underwriter.baseUrl:underwriter}",
    configuration = [FeignConfig::class]
)
interface UnderwriterClient {
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

    @PostMapping("/_/v1/quotes/{completeQuoteId}/signFromHope")
    fun signQuoteForNewContract(
        @PathVariable completeQuoteId: UUID,
        @RequestBody request: SignQuoteFromHopeRequestDto
    ): ResponseEntity<Any>

    @GetMapping("/_/v1/quotes/schema/{quoteId}")
    fun getSchemaByQuoteId(@PathVariable quoteId: UUID): ResponseEntity<JsonNode>

    @GetMapping("/_/v1/quotes/schema/{quoteId}/data")
    fun getSchemaDataByQuoteId(@PathVariable quoteId: UUID): ResponseEntity<JsonNode>

    @GetMapping("/_/v1/quotes/schema/contract/{contractType}")
    fun getSchemaForContractType(@PathVariable contractType: String): ResponseEntity<JsonNode>

    @PostMapping("/_/v1/quotes/schema/{quoteId}/update")
    fun updateQuoteBySchemaData(
        @PathVariable quoteId: UUID,
        @RequestBody body: JsonNode,
        @RequestParam underwritingGuidelinesBypassedBy: String?
    ): QuoteResponseDto

    @PostMapping("/_/v1/quotes/schema/{memberId}/create")
    fun createQuoteForMemberBySchemaData(
        @PathVariable memberId: String,
        @RequestBody body: JsonNode,
        @RequestParam underwritingGuidelinesBypassedBy: String?
    ): QuoteResponseDto
}
