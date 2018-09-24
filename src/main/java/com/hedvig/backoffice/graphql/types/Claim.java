package com.hedvig.backoffice.graphql.types;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;
import lombok.Value;

@Value
public class Claim {
  UUID id;
  String recordingUrl;
  ClaimState state;
  String type;
  MonetaryAmount reserves;
  ZonedDateTime registrationDate;
  String memberId;
  List<ClaimPayment> payments;
  List<ClaimNote> notes;
  List<ClaimEvent> events;

  public static Claim fromDTO(com.hedvig.backoffice.services.claims.dto.Claim dto) {
    return new Claim(UUID.fromString(dto.getId()), dto.getAudioURL(),
        ClaimState.valueOf(dto.getState().toString()), dto.getType(),
        Money.of(dto.getReserve(), "SEK"), // TODO Support other currencies other than SEK in
                                           // claims-service
        dto.getRegistrationDate().atZone(ZoneId.of("Europe/Stockholm")), // TODO Do not hardcode
                                                                         // time zone
        dto.getUserId(),
        dto.getPayments().stream().map(paymentDto -> ClaimPayment.fromDto(paymentDto))
            .collect(Collectors.toList()),
        dto.getNotes().stream().map(noteDto -> ClaimNote.fromDTO(noteDto))
            .collect(Collectors.toList()),
        dto.getEvents().stream().map(eventDto -> ClaimEvent.fromDTO(eventDto))
            .collect(Collectors.toList()));
  }
}
