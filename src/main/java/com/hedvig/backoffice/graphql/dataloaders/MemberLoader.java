package com.hedvig.backoffice.graphql.dataloaders;

import com.hedvig.backoffice.graphql.types.Member;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.springframework.stereotype.Component;

@Component
public class MemberLoader extends DataLoader<String, Member> {
  public MemberLoader(MemberBatchLoader memberBatchLoader) {
    // Caching must be disabled due to this being a globally scoped dataloader. We should try to get
    // a per-request scoped loader later on.
    super(memberBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
  }
}
