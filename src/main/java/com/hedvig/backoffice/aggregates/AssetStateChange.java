package com.hedvig.backoffice.aggregates;

import com.hedvig.common.commands.AssetStateChangeCommand;
import com.hedvig.common.events.AssetStateChangeEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor
public class AssetStateChange {

    @AggregateIdentifier
    private String id;

    @CommandHandler
    public AssetStateChange(AssetStateChangeCommand command) {
        this.id = command.getId();
        apply(new AssetStateChangeEvent(command.getId(), command.getAssetId(), command.getState()));
    }

}
