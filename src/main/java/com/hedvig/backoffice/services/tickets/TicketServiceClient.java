package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.graphql.types.RemindNotification;
import com.hedvig.backoffice.services.tickets.dto.TicketDto;
import com.hedvig.backoffice.services.tickets.dto.TicketStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
  name= "ticket-service",
  url = "${tickets.baseUrl}",
  configuration = FeignConfig.class)

public interface TicketServiceClient {
  @GetMapping("/_/tickets/{id}")
  TicketDto getTicket (@PathVariable("id") String id);

  @GetMapping ("/_/tickets/all")
  List<TicketDto> getTickets ();

  @PostMapping("/_/tickets/new/{id}")
  void createTicket (@PathVariable String id, @RequestBody TicketDto ticket);

  @PostMapping("/_/tickets/description/{id}")
  TicketDto changeDescription (@PathVariable String id, @RequestBody String newDescription);

  @PostMapping("/_/tickets/assign/{id}")
  TicketDto changeAssignedTo (@PathVariable String id, @RequestBody String assignTo);

  @PostMapping("/_/tickets/status/{id}")
  TicketDto changeStatus (@PathVariable String id, @RequestBody TicketStatus newStatus);

  @PostMapping("/_/tickets/reminder/{id}")
  TicketDto changeReminder (@PathVariable String id, @RequestBody RemindNotification newReminder);

}
