package com.hedvig.backoffice.services.claims.dto

import com.hedvig.backoffice.services.claims.ClaimState
import java.math.BigDecimal
import java.util.UUID

class Claim(
    val audioURL: String? = null,
    val state: ClaimState,
    val reserve: BigDecimal? = null,
    val type: String? = null,
    val claimSource: ClaimSource,
    val notes: List<ClaimNote>,
    val transcriptions: List<ClaimTranscription>,
    val payments: List<ClaimPayment>,
    val assets: List<ClaimAsset>,
    val events: List<ClaimEvent>,
    val data: List<ClaimData>,
    val coveringEmployee: Boolean,
    val claimFiles: List<ClaimFileDTO>,
    val contractId: UUID? = null
) : ClaimBackOffice()
