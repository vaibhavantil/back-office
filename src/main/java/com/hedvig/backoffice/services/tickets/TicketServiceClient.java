package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.graphql.types.RemindNotification;
import com.hedvig.backoffice.services.tickets.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(
  name = "ticket-service",
  url = "${tickets.baseUrl:ticket-service}",
  configuration = FeignConfig.class)

public interface TicketServiceClient {
  @GetMapping("/_/tickets/{id}")
  TicketDto getTicket(@PathVariable("id") UUID id);

  @GetMapping("/_/tickets/all")
  List<TicketDto> getTickets();

  @PostMapping("/_/tickets/new/")
  TicketDto createTicket(@RequestBody CreateTicketDto ticket);

  @PostMapping("/_/tickets/description/{id}")
  TicketDto changeDescription(@PathVariable UUID id,
                              @RequestBody NewDescriptionDto newDescription);

  @PostMapping("/_/tickets/assign/{id}")
  TicketDto changeAssignedTo(@PathVariable UUID id,
                             @RequestBody NewAssignedToDto assignedTo);

  @PostMapping("/_/tickets/status/{id}")
  TicketDto changeStatus(@PathVariable UUID id,
                         @RequestBody NewStatusDto newStatus);

  @PostMapping("/_/tickets/reminder/{id}")
  TicketDto changeReminder(@PathVariable UUID id,
                           @RequestBody NewReminderDto newReminder);

}
