package com.hedvig.backoffice.services.underwriter

import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.underwriter.dtos.ActivateQuoteRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteInputDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteRequestDto
import com.hedvig.backoffice.services.underwriter.dtos.QuoteResponseDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
    @RequestBody quoteDto: QuoteInputDto
  ): QuoteDto

  @PostMapping("/_/v1/quotes/{quoteId}/complete")
  fun completeQuote(@PathVariable("quoteId") quoteId: UUID)

  @PostMapping("/_/v1/quotes/{quoteId}/activate")
  fun activateQuote(@PathVariable("quoteId") quoteId: UUID, @RequestBody body: ActivateQuoteRequestDto): QuoteDto

  @GetMapping("/_/v1/quotes/members/{memberId}")
  fun getQuotes(@PathVariable("memberId") memberId: String): List<QuoteDto>

  @GetMapping("/_/v1/quotes/{quoteId}")
  fun getQuote(@PathVariable("quoteId") quoteId: UUID): QuoteDto
}
