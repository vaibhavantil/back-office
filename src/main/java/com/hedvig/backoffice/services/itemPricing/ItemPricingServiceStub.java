package com.hedvig.backoffice.services.itemPricing;

import com.google.common.collect.Lists;
import com.hedvig.backoffice.services.itemPricing.ItemPricingServiceClient;
import com.hedvig.backoffice.services.itemPricing.dto.CategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterSuggestionDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchDTO;
import com.hedvig.backoffice.services.itemPricing.dto.PayloadDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemDTO;
import com.hedvig.backoffice.services.itemPricing.dto.PricepointDTO;
import java.util.List;
import java.util.ArrayList;

public class ItemPricingServiceStub implements ItemPricingService {

  @Override
  public List<CategoryDTO> getCategories() {
    return new ArrayList<CategoryDTO>();
  }

  @Override
  public ItemSearchDTO getItems(PayloadDTO payload) {
    return new ItemSearchDTO(new ArrayList<ItemDTO>(), new ArrayList<FilterSuggestionDTO>());
  }

  @Override
  public List<PricepointDTO> getPrices(String date, List<String> ids) {
    return new ArrayList<PricepointDTO>();
  }

}
