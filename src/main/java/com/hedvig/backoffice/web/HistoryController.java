package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.history.HistoryService;
import com.hedvig.backoffice.chat.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/{id}")
    public List<MessageDTO> messages(@PathVariable String id) {
        return historyService.loadHistory(id);
    }

}
