package com.hedvig.backoffice.services.itemPricing;

import com.hedvig.backoffice.services.itemPricing.dto.CategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.SearchItemDTO;
import com.hedvig.backoffice.services.itemPricing.dto.InventoryItemDTO;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

public class ItemPricingServiceStub implements ItemPricingService {

  @Override
  public List<String> getCategories() {

    ArrayList<String> categories = new ArrayList<>();

    categories.add("Mobiltelefoner");
    categories.add("TV");

    return categories;
  }

  @Override
  public List<SearchItemDTO> getSuggestions(String query) {

    ArrayList<SearchItemDTO> items = new ArrayList<>();

    items.add(new SearchItemDTO("123456", "Samsung Galaxy S8", "https://google.se/"));

    return items;
  }

  @Override
  public List<InventoryItemDTO> getInventory(String claimId) {

    ArrayList<InventoryItemDTO> inventory = new ArrayList<>();

    inventory.add(new InventoryItemDTO("4fe5c0f3-6790-4f49-bb1b-fd7397601232", "4fe5c0f3-6790-4f49-bb1b-fd7397609442", "Samsung Something", "Mobiltelefoner", new BigDecimal(5000)));

    return inventory;
  }

  @Override
  public boolean addInventoryItem(InventoryItemDTO item) {
    return true;
  }

  @Override
  public boolean removeInventoryItem(String inventoryItemId) {
    return true;
  }

}
