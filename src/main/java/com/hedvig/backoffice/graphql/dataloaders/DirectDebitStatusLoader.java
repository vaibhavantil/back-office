package com.hedvig.backoffice.graphql.dataloaders;

import com.hedvig.backoffice.graphql.types.DirectDebitStatus;

import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

@Component
public class DirectDebitStatusLoader extends DataLoader<String, DirectDebitStatus> {
    public DirectDebitStatusLoader(DirectDebitStatusBatchLoader directDebitStatusBatchLoader) {
        super(directDebitStatusBatchLoader);
    }
}
