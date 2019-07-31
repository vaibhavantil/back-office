package com.hedvig.backoffice.services.itemPricing;

import com.hedvig.backoffice.services.itemPricing.dto.CategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterSuggestionDTO;
import com.hedvig.backoffice.services.itemPricing.dto.PayloadDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemBodyDTO;
import com.hedvig.backoffice.services.itemPricing.dto.PricepointDTO;
import java.util.List;
import java.util.ArrayList;

public class ItemPricingServiceImpl implements ItemPricingService {

  private final ItemPricingServiceClient itemPricingServiceClient;

  public ItemPricingServiceImpl(ItemPricingServiceClient itemPricingServiceClient) {
    this.itemPricingServiceClient = itemPricingServiceClient;
  }

  @Override
  public List<CategoryDTO> getCategories() {
    return this.itemPricingServiceClient.getCategories();
  }

  @Override
  public ItemSearchDTO getItems(PayloadDTO payload) {
    return this.itemPricingServiceClient.getItems(payload);
  }

  @Override
  public List<PricepointDTO> getPrices(String date, List<String> ids) {
    return this.itemPricingServiceClient.getPrices(date, ids);
  }
}

