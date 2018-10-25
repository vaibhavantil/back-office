package com.hedvig.backoffice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceSearchResultDTO {
  List<InsuranceStatusDTO> products;
  Integer page;
  Integer totalPages;
}
