package com.hedvig.backoffice.graphql.types;

import com.hedvig.backoffice.services.claims.dto.ClaimData;
import com.hedvig.backoffice.services.product_pricing.dto.contract.Contract;
import lombok.Value;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Value
public class Claim {
  UUID id;
  String recordingUrl;
  public ClaimState state;
  public String _type;
  MonetaryAmount reserves;
  Instant registrationDate;
  public String memberId;
  List<ClaimPayment> payments;
  List<ClaimNote> notes;
  List<ClaimTranscription> transcriptions;
  List<ClaimEvent> events;
  public List<ClaimData> _claimData;
  boolean coveringEmployee;
  public List<ClaimFile> claimFiles;
  public UUID contractId;

  public static Claim fromDTO(com.hedvig.backoffice.services.claims.dto.Claim dto) {
    return new Claim(UUID.fromString(dto.getId()), dto.getAudioURL(),
      ClaimState.valueOf(dto.getState().toString()), dto.getType(),
      dto.getReserve() != null ? Money.of(dto.getReserve(), "SEK") : null, // TODO Support other
      // currencies other
      // than SEK in
      // claims-service
      dto.getDate().atZone(ZoneId.of("Europe/Stockholm")).toInstant(), // TODO Do not
      // hardcode
      // time zone
      dto.getUserId(),
      dto.getPayments().stream().map(ClaimPayment::fromDto)
        .collect(Collectors.toList()),
      dto.getNotes().stream().map(ClaimNote::fromDTO)
        .collect(Collectors.toList()),
      (dto.getTranscriptions() != null) ? dto.getTranscriptions().stream().map(ClaimTranscription.Companion::fromDTO)
        .collect(Collectors.toList()) : Collections.emptyList(),
      dto.getEvents().stream().map(ClaimEvent::fromDTO)
        .collect(Collectors.toList()),
      dto.getData(),
      dto.isCoveringEmployee(),
      dto.getClaimFiles().stream().map(ClaimFile.Companion::fromDTO)
        .collect(Collectors.toList()
      ),
      dto.getContractId()
    );
  }

}
