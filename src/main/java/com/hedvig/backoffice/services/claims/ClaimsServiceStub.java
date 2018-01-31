package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.users.UserService;
import com.hedvig.backoffice.services.users.UserServiceException;
import com.hedvig.backoffice.web.dto.ClaimDTO;
import com.hedvig.backoffice.web.dto.ClaimEventDTO;
import com.hedvig.backoffice.web.dto.ClaimTypeDTO;
import com.hedvig.backoffice.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClaimsServiceStub implements ClaimsService {

    private List<ClaimDTO> claims;
    private List<ClaimTypeDTO> types;
    private Map<String, List<ClaimEventDTO>> events;

    @Autowired
    public ClaimsServiceStub(UserService userService) throws ClaimException, UserServiceException {
        events = new HashMap<>();

        Map<String, Boolean> required = new HashMap<>();
        required.put("required 1", false);
        required.put("required 2", false);

        Map<String, Boolean> additional = new HashMap<>();
        additional.put("additional 1", false);
        additional.put("additional 2", false);

        types = Arrays.asList(new ClaimTypeDTO("THEFT", required, additional),
                new ClaimTypeDTO("BURN", required, additional),
                new ClaimTypeDTO("SINK", required, additional));

        List<UserDTO> users = userService.list();

        claims = IntStream.range(0, 10).mapToObj(i -> {
            String id = UUID.randomUUID().toString();
            UserDTO user = users.size() < i ? users.get(i) : users.get(users.size() - 1);

            return new ClaimDTO(id,
                    user.getHid(),
                    ClaimStatus.OPEN,
                    types.get(0),
                    "http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg",
                    LocalDate.now());
        }).collect(Collectors.toList());
    }

    @Override
    public List<ClaimDTO> list() throws ClaimException {
        return claims;
    }

    @Override
    public ClaimDTO find(String id) throws ClaimException {
        return claims
                .stream()
                .filter(c -> c.getId().contains(id))
                .findAny()
                .orElseThrow(() -> new ClaimNotFoundException("claim with id " + id + " not found"));
    }

    @Override
    public List<ClaimTypeDTO> types() throws ClaimException {
        return types;
    }

    @Override
    public void save(ClaimDTO dto) throws ClaimException {
        for (int i = 0; i < claims.size(); i++) {
            ClaimDTO claim = claims.get(i);
            if (claim.getId().equals(dto.getId())) {
                claims.set(i, dto);
                break;
            }
        }
    }

    @Override
    public List<ClaimEventDTO> events(String id) throws ClaimException {
        find(id);
        return Optional.ofNullable(events.get(id))
                .orElse(new ArrayList<>());
    }

    @Override
    public void addEvent(ClaimEventDTO dto) throws ClaimException {
        List<ClaimEventDTO> claimEvents = events.computeIfAbsent(dto.getClaimId(), k -> new ArrayList<>());
        claimEvents.add(dto);
    }

}
