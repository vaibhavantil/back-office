package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.web.dto.ClaimDTO;
import com.hedvig.backoffice.web.dto.ClaimEventDTO;
import com.hedvig.backoffice.web.dto.ClaimTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClaimUpdateServiceImpl implements ClaimUpdateService {

    private final ClaimsService claimsService;

    @Autowired
    public ClaimUpdateServiceImpl(ClaimsService claimsService) {
        this.claimsService = claimsService;
    }

    @Override
    public void changeType(String id, ClaimTypeDTO dto) throws ClaimException {
        ClaimDTO claim = claimsService.find(id);
        claim.setType(dto);
        claimsService.save(claim);

        ClaimEventDTO event = new ClaimEventDTO(id, "type changed to " + dto.getName());
        claimsService.addEvent(event);
    }

    @Override
    public void changeStatus(String id, ClaimStatus status) throws ClaimException {
        ClaimDTO claim = claimsService.find(id);
        claim.setStatus(status);
        claimsService.save(claim);

        ClaimEventDTO event = new ClaimEventDTO(id, "status changed to " + status.toString());
        claimsService.addEvent(event);
    }
}
