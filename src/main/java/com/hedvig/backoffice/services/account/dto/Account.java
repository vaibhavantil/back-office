package com.hedvig.backoffice.services.account.dto;

import lombok.Value;

import java.util.List;

@Value
public class Account {
  String id;
  List<Entry> entries;
}
