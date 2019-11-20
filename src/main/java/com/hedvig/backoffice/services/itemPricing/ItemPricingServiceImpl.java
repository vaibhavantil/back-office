package com.hedvig.backoffice.services.itemPricing;

import com.hedvig.backoffice.services.itemPricing.dto.CategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterSuggestionDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchQueryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemPricepointDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ClaimInventoryItemDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterDTO;
import java.util.List;

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
  public ItemSearchDTO getItems(ItemSearchQueryDTO payload) {
    return this.itemPricingServiceClient.getItems(payload);
  }

  @Override
  public List<ItemPricepointDTO> getPrices(String date, List<String> ids) {
    return this.itemPricingServiceClient.getPrices(date, ids);
  }

  @Override
  public List<ClaimInventoryItemDTO> getInventory(String claimId) {
    return this.itemPricingServiceClient.getInventory(claimId);
  }

  @Override
  public boolean addInventoryItem(ClaimInventoryItemDTO item) {
    return this.itemPricingServiceClient.addInventoryItem(item);
  }

  @Override
  public boolean removeInventoryItem(String inventoryItemId) {
    return this.itemPricingServiceClient.removeInventoryItem(inventoryItemId);
  }

  @Override
  public boolean removeInventoryFilters(String inventoryItemId) {
    return this.itemPricingServiceClient.removeInventoryFilters(inventoryItemId);
  }

  @Override
  public List<FilterSuggestionDTO> getAllFilters(String categoryId) {
    return this.itemPricingServiceClient.getAllFilters(categoryId);
  }

  @Override
  public List<FilterDTO> getInventoryItemFilters(String inventoryItemId) {
    return this.itemPricingServiceClient.getInventoryItemFilters(inventoryItemId);
  }
}

