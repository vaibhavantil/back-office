package com.hedvig.backoffice.services.itemPricing;

import com.hedvig.backoffice.services.itemPricing.dto.CategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterSuggestionDTO;
import com.hedvig.backoffice.services.itemPricing.dto.PayloadDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemBodyDTO;
import com.hedvig.backoffice.services.itemPricing.dto.PricepointDTO;
import com.hedvig.backoffice.services.itemPricing.dto.InventoryItemDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterPayloadDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterDTO;
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

