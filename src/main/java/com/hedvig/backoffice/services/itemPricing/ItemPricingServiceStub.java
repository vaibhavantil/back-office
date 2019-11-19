package com.hedvig.backoffice.services.itemPricing;

import com.hedvig.backoffice.services.itemPricing.dto.CategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterSuggestionDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchQueryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemPricepointDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ClaimInventoryItemDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterDTO;
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
  public ItemSearchDTO getItems(ItemSearchQueryDTO payload) {

    ArrayList<ItemDTO> items = new ArrayList<ItemDTO>();

    items.add(new ItemDTO("1", "123", "Samsung Galaxy S8"));

    return new ItemSearchDTO(items, new ArrayList<FilterSuggestionDTO>());
  }

  @Override
  public List<ItemPricepointDTO> getPrices(String date, List<String> ids) {

    ArrayList<ItemPricepointDTO> prices = new ArrayList<ItemPricepointDTO>();

    prices.add(new ItemPricepointDTO("123:123", "123", "2019-06-07", new BigDecimal(6000), new BigDecimal(6010), new BigDecimal(6020)));

    return prices;
  }

  @Override
  public List<ClaimInventoryItemDTO> getInventory(String claimId) {

    ArrayList<ClaimInventoryItemDTO> inventory = new ArrayList<ClaimInventoryItemDTO>();

    inventory.add(new ClaimInventoryItemDTO("4fe5c0f3-6790-4f49-bb1b-fd7397601232", "4fe5c0f3-6790-4f49-bb1b-fd7397609442", "Samsung Something", "Mobiltelefoner", "1", new BigDecimal(5000), "Custom", null, null, null, null));

    return inventory;
  }

  @Override
  public boolean addInventoryItem(ClaimInventoryItemDTO item) {
    return true;
  }

  @Override
  public boolean removeInventoryItem(String inventoryItemId) {
    return true;
  }

  @Override
  public boolean removeInventoryFilters(String inventoryItemId) {
    return true;
  }

  @Override
  public List<FilterSuggestionDTO> getAllFilters(String categoryId) {
    ArrayList<FilterSuggestionDTO> filters = new ArrayList<FilterSuggestionDTO>();

    filters.add(new FilterSuggestionDTO("Some Filter", new ArrayList<String>(), new ArrayList<String>()));

    return filters;
  }

  @Override
  public List<FilterDTO> getInventoryItemFilters(String inventoryItemId) {
    ArrayList<FilterDTO> filters = new ArrayList<FilterDTO>();

    filters.add(new FilterDTO("FilterName", "FilterValue"));

    return null;
  }

}
