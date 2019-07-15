package com.hedvig.backoffice.services.tickets.dto;

import com.hedvig.backoffice.graphql.types.TicketInput;
import lombok.Value;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class CreateTicketDto {
    String createdBy;
    String assignedTo;
    float priority; //0 <= priority <= 1
    TicketType type;
    LocalDate remindNotificationDate;
    LocalTime remindNotificationTime;
    String remindMessage;
    String description;
    TicketStatus status;

    public static CreateTicketDto fromTicketInput(TicketInput t, String createdBy) {
      return new CreateTicketDto (
        createdBy,
        t.getAssignedTo(),
        t.getPriority(),
        t.getType(),
        t.getRemindNotificationDate(),
        t.getRemindNotificationTime(),
        t.getRemindMessage(),
        t.getDescription(),
        t.getStatus()
      );
    }
}
