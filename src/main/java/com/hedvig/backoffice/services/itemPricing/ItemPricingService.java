package com.hedvig.backoffice.services.itemPricing;

import com.hedvig.backoffice.services.itemPricing.dto.CategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchDTO;
import com.hedvig.backoffice.services.itemPricing.dto.PayloadDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemBodyDTO;
import com.hedvig.backoffice.services.itemPricing.dto.PricepointDTO;
import java.util.List;


public interface ItemPricingService {
  List<CategoryDTO> getCategories();
  ItemSearchDTO getItems(PayloadDTO payload);
  List<PricepointDTO> getPrices(String date, List<String> ids);
}
