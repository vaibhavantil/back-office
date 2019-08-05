package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.config.feign.FeignConfig;
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
  UUID createTicket(@RequestBody CreateTicketDto ticket);

  @PostMapping("/_/tickets/description/{id}")
  void changeDescription(@PathVariable UUID id,
                              @RequestBody ChangeDescriptionDto newDescription);

  @PostMapping("/_/tickets/assign/{id}")
  void changeAssignedTo(@PathVariable UUID id,
                             @RequestBody ChangeAssignToDto assignedTo);

  @PostMapping("/_/tickets/status/{id}")
  void changeStatus(@PathVariable UUID id,
                         @RequestBody ChangeStatusDto newStatus);

  @PostMapping("/_/tickets/reminder/{id}")
  void changeReminder(@PathVariable UUID id,
                           @RequestBody ChangeReminderDto newReminder);

  @PostMapping("/_/tickets/priority/{id}")
  void changePriority(@PathVariable UUID id,
                           @RequestBody ChangePriorityDto newPriority);


}
