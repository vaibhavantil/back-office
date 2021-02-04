package com.hedvig.backoffice.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import com.hedvig.backoffice.config.Money
import com.hedvig.backoffice.services.itemizer.ItemizerService
import com.hedvig.backoffice.services.itemizer.dto.ClaimItem
import com.hedvig.backoffice.services.itemizer.dto.ClaimItemValuation
import com.hedvig.backoffice.services.itemizer.dto.request.GetClaimItemValuationRequest
import com.hedvig.graphql.commons.type.MonetaryAmountV2
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

@Component
class ClaimItemResolver(
    private val itemizerService: ItemizerService
) : GraphQLResolver<ClaimItem> {
    fun getValuation(claimItem: ClaimItem, typeOfContract: String): ClaimItemValuation? {
        println("AAAAAAAAAA: ${typeOfContract}")
        return ClaimItemValuation(
            depreciatedValue = MonetaryAmountV2("2000", "SEK"),
            valuationRule = null,
            explanation = "I don't know"
        )
    }
}
