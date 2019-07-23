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
import java.math.BigDecimal;

public class ItemPricingServiceStub implements ItemPricingService {

  @Override
  public List<CategoryDTO> getCategories() {

    ArrayList<CategoryDTO> categories = new ArrayList<CategoryDTO>();

    categories.add(new CategoryDTO("1", "Mobiltelefoner"));
    categories.add(new CategoryDTO("2", "TV"));

    return categories;
  }

  @Override
  public ItemSearchDTO getItems(PayloadDTO payload) {

    ArrayList<ItemDTO> items = new ArrayList<ItemDTO>();

    items.add(new ItemDTO("1", "123", "Samsung Galaxy S8"));

    return new ItemSearchDTO(items, new ArrayList<FilterSuggestionDTO>());
  }

  @Override
  public List<PricepointDTO> getPrices(String date, List<String> ids) {

    ArrayList<PricepointDTO> prices = new ArrayList<PricepointDTO>();

    prices.add(new PricepointDTO("123:123", "123", "2019-06-07", new BigDecimal(6000), new BigDecimal(6010), new BigDecimal(6020)));

    return prices;
  }

}
