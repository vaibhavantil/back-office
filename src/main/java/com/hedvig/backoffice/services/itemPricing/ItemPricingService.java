package com.hedvig.backoffice.services.itemPricing;

import com.hedvig.backoffice.services.itemPricing.dto.InventoryItemDTO;
import com.hedvig.backoffice.services.itemPricing.dto.SearchItemDTO;
import java.util.List;

public interface ItemPricingService {
  List<String> getCategories();
  List<SearchItemDTO> getSuggestions(String query);
  SearchItemDTO getItemDetails(List<String> ids);
  List<InventoryItemDTO> getInventory(String claimId);
  boolean addInventoryItem(InventoryItemDTO item);
  boolean removeInventoryItem(String inventoryItemId);
}
