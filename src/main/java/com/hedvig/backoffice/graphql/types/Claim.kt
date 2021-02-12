package com.hedvig.backoffice.graphql.types

import com.hedvig.backoffice.services.claims.dto.ClaimData
import org.javamoney.moneta.Money
import java.time.Instant
import java.time.ZoneId
import java.util.UUID
import javax.money.MonetaryAmount
import com.hedvig.backoffice.services.claims.dto.Claim as ClaimDto

data class Claim(
    var id: UUID,
    var recordingUrl: String? = null,
    var state: ClaimState? = null,
    var _type: String? = null,
    var reserves: MonetaryAmount? = null,
    var registrationDate: Instant? = null,
    var memberId: String,
    var payments: List<ClaimPayment>,
    var notes: List<ClaimNote>,
    var transcriptions: List<ClaimTranscription>,
    var events: List<ClaimEvent>,
    var _claimData: List<ClaimData>,
    var coveringEmployee: Boolean = false,
    var claimFiles: List<ClaimFile>,
    var contractId: UUID? = null
) {

    companion object {
        fun fromDTO(dto: ClaimDto) = Claim(
            id = UUID.fromString(dto.id),
            recordingUrl = dto.audioURL,
            state = ClaimState.valueOf(dto.state.toString()),
            _type = dto.type,
            reserves = if (dto.reserve != null) Money.of(dto.reserve, "SEK") else null, // TODO Support other currencies other than SEK in claims-service
            registrationDate = dto.date.atZone(ZoneId.of("Europe/Stockholm")).toInstant(), // TODO Do not hardcode time zone
            memberId = dto.userId,
            payments = dto.payments.map { ClaimPayment.fromDto(it) },
            notes = dto.notes.map { ClaimNote.fromDTO(it) },
            transcriptions = dto.transcriptions.map { ClaimTranscription.fromDTO(it) },
            events = dto.events.map { ClaimEvent.fromDTO(it) },
            _claimData = dto.data,
            coveringEmployee = dto.coveringEmployee,
            claimFiles = dto.claimFiles.map { ClaimFile.fromDTO(it) },
            contractId = dto.contractId
        )
    }
}
