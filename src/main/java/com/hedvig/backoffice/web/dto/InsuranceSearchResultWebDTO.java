package com.hedvig.backoffice.web.dto;

import com.hedvig.backoffice.services.product_pricing.dto.InsuranceSearchResultDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class InsuranceSearchResultWebDTO {
  List<InsuranceStatusWebDTO> products;
  Integer page;
  Integer totalPages;

  public InsuranceSearchResultWebDTO(InsuranceSearchResultDTO res) {
    this.page = res.getPage();
    this.totalPages = res.getTotalPages();
    this.products = res.getProducts().stream()
      .map(InsuranceStatusWebDTO::new)
      .collect(Collectors.toList());
  }
}
