package com.hedvig.backoffice.services.itemPricing;

import com.hedvig.backoffice.services.itemPricing.dto.CategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchQueryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemPricepointDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ClaimInventoryItemDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterSuggestionDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterDTO;
import java.util.List;

public interface ItemPricingService {
  List<CategoryDTO> getCategories();
  ItemSearchDTO getItems(ItemSearchQueryDTO payload);
  List<ItemPricepointDTO> getPrices(String date, List<String> ids);
  List<ClaimInventoryItemDTO> getInventory(String claimId);
  boolean addInventoryItem(ClaimInventoryItemDTO item);
  boolean removeInventoryItem(String inventoryItemId);
  boolean removeInventoryFilters(String inventoryItemId);
  List<FilterSuggestionDTO> getAllFilters(String categoryId);
  List<FilterDTO> getInventoryItemFilters(String inventoryItemId);
}
