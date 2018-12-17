package com.hedvig.backoffice.graphql.dataloaders;

import java.util.UUID;
import com.hedvig.backoffice.graphql.types.Claim;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.springframework.stereotype.Component;

@Component
public class ClaimLoader extends DataLoader<UUID, Claim> {
  public ClaimLoader(ClaimBatchLoader claimBatchLoader) {
    super(claimBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
  }
}
