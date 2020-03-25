package com.hedvig.backoffice.services.itemPricing;

import com.hedvig.backoffice.services.itemPricing.dto.ItemCategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.SearchItemDTO;
import com.hedvig.backoffice.services.itemPricing.dto.InventoryItemDTO;
import java.util.List;

public class ItemPricingServiceImpl implements ItemPricingService {

  private final ItemPricingServiceClient itemPricingServiceClient;

  public ItemPricingServiceImpl(ItemPricingServiceClient itemPricingServiceClient) {
    this.itemPricingServiceClient = itemPricingServiceClient;
  }

  @Override
  public List<ItemCategoryDTO> getCategories() {
    return this.itemPricingServiceClient.getCategories();
  }

  @Override
  public List<SearchItemDTO> getSuggestions(String query) {
    return this.itemPricingServiceClient.getSuggestions(query);
  }

  @Override
  public SearchItemDTO getItemDetails(List<String> ids) {
    return this.itemPricingServiceClient.getItemDetails(ids);
  }

  @Override
  public List<InventoryItemDTO> getInventory(String claimId) {
    return this.itemPricingServiceClient.getInventory(claimId);
  }

  @Override
  public boolean addInventoryItem(InventoryItemDTO item) {
    return this.itemPricingServiceClient.addInventoryItem(item);
  }

  @Override
  public boolean removeInventoryItem(String inventoryItemId) {
    return this.itemPricingServiceClient.removeInventoryItem(inventoryItemId);
  }
}

