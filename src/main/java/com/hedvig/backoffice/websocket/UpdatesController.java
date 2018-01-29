package com.hedvig.backoffice.websocket;

import com.hedvig.backoffice.services.updates.UpdateType;
import com.hedvig.backoffice.services.updates.UpdatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@MessageMapping("/updates")
public class UpdatesController {

    private final UpdatesService updatesService;

    @Autowired
    public UpdatesController(UpdatesService updatesService) {
        this.updatesService = updatesService;
    }

    @SubscribeMapping("/clear/{type}")
    public void clear(@DestinationVariable UpdateType type, Principal principal) {
        updatesService.clear(principal.getName(), type);
    }

    @SubscribeMapping("/")
    public void updates(Principal principal) {
        updatesService.updates(principal.getName());
    }

}
