package com.hedvig.backoffice.graphql.types;

import java.time.LocalDate;
import lombok.Value;

@Value
public class ClaimInformationInput {
  String location;
  LocalDate date;
  String item;
  String policeReport;
  String receipt;
  String ticket;
}
