package com.hedvig.backoffice.services.claims.dto

import com.hedvig.backoffice.services.claims.ClaimState
import java.math.BigDecimal
import java.util.UUID

data class Claim(
    var audioURL: String? = null,
    var state: ClaimState,
    var reserve: BigDecimal? = null,
    var type: String? = null,
    val claimSource: ClaimSource,
    val notes: List<ClaimNote>,
    val transcriptions: List<ClaimTranscription>,
    val payments: List<ClaimPayment>,
    val assets: List<ClaimAsset>,
    val events: List<ClaimEvent>,
    val data: List<ClaimData>,
    var coveringEmployee: Boolean,
    val claimFiles: List<ClaimFileDTO>,
    val contractId: UUID? = null
) : ClaimBackOffice()
