package com.hedvig.backoffice.services.itemPricing;

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

    ArrayList<String> ids = new ArrayList<>();

    ids.add("3816379");

    items.add(new SearchItemDTO("Apple iPhone 8 64GB", "Mobiltelefoner", "https://www.pricerunner.se/pl/1-3816379/Mobiltelefoner/Apple-iPhone-8-64GB-priser", ids, new BigDecimal(6000)));

    return items;
  }

  @Override
  public SearchItemDTO getItemDetails(List<String> ids) {
    return new SearchItemDTO("Apple iPhone 8 64GB", "Mobiltelefoner", "https://www.pricerunner.se/pl/1-3816379/Mobiltelefoner/Apple-iPhone-8-64GB-priser", ids, new BigDecimal(6000));
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
