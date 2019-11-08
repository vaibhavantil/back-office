package com.hedvig.backoffice.services.itemPricing;

import com.hedvig.backoffice.services.itemPricing.dto.CategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchDTO;
import com.hedvig.backoffice.services.itemPricing.dto.PayloadDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemBodyDTO;
import com.hedvig.backoffice.services.itemPricing.dto.PricepointDTO;
import com.hedvig.backoffice.services.itemPricing.dto.InventoryItemDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterPayloadDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterSuggestionDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterDTO;
import java.util.List;


public interface ItemPricingService {
  List<CategoryDTO> getCategories();
  ItemSearchDTO getItems(PayloadDTO payload);
  List<PricepointDTO> getPrices(String date, List<String> ids);
  List<InventoryItemDTO> getInventory(String claimId);
  boolean addInventoryItem(InventoryItemDTO item);
  boolean removeInventoryItem(String inventoryItemId);
  boolean removeInventoryFilters(String inventoryItemId);
  List<FilterSuggestionDTO> getAllFilters(String categoryId);
  List<FilterDTO> getInventoryItemFilters(String inventoryItemId);


}
