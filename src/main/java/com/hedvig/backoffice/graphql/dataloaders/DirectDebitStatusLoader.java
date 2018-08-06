package com.hedvig.backoffice.graphql.dataloaders;

import com.hedvig.backoffice.graphql.types.DirectDebitStatus;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.springframework.stereotype.Component;

@Component
public class DirectDebitStatusLoader extends DataLoader<String, DirectDebitStatus> {
  public DirectDebitStatusLoader(DirectDebitStatusBatchLoader directDebitStatusBatchLoader) {
    // Caching must be disabled due to this being a globally scoped dataloader. We should try to get
    // a per-request scoped loader later on.
    super(directDebitStatusBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
  }
}
