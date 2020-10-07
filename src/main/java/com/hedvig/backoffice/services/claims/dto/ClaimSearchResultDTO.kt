package com.hedvig.backoffice.services.claims.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimSearchResultDTO {
  List<Claim> claims;
  Integer page;
  Integer totalPages;
}
