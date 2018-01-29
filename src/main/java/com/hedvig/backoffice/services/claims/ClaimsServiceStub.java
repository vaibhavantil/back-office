package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.web.dto.ClaimDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClaimsServiceStub implements ClaimsService {

    private List<ClaimDTO> claims;

    public ClaimsServiceStub() {
        claims = IntStream.range(0, 15).mapToObj(i -> {
            String id = UUID.randomUUID().toString();
            return new ClaimDTO(id, "Claim " + i, LocalDate.now());
        }).collect(Collectors.toList());
    }

    @Override
    public List<ClaimDTO> list() throws ClaimsServiceException {
        return claims;
    }

    @Override
    public ClaimDTO find(String id) throws ClaimsServiceException, ClaimNotFoundException {
        return claims
                .stream()
                .filter(c -> c.getId().contains(id))
                .findAny()
                .orElseThrow(() -> new ClaimNotFoundException("claim wit id " + id + "not found"));
    }

}
