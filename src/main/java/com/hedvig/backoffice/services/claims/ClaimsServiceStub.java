package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.users.UserService;
import com.hedvig.backoffice.services.users.UserServiceException;
import com.hedvig.backoffice.web.dto.ClaimDTO;
import com.hedvig.backoffice.web.dto.ClaimTypeDTO;
import com.hedvig.backoffice.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClaimsServiceStub implements ClaimsService {

    private List<ClaimDTO> claims;

    @Autowired
    public ClaimsServiceStub(UserService userService) throws UserServiceException, ClaimsServiceException {
        List<UserDTO> users = userService.list();
        List<ClaimTypeDTO> types = types();

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

    @Override
    public void changeStatus(String id, ClaimStatus status) throws ClaimsServiceException, ClaimNotFoundException {
        ClaimDTO claim = find(id);
        claim.setStatus(status);
    }

    @Override
    public List<ClaimTypeDTO> types() throws ClaimsServiceException {
        Map<String, Boolean> required = new HashMap<>();
        required.put("required 1", false);
        required.put("required 2", false);

        Map<String, Boolean> additional = new HashMap<>();
        additional.put("additional 1", false);
        additional.put("additional 2", false);


        return Arrays.asList(new ClaimTypeDTO("THEFT", required, additional),
                new ClaimTypeDTO("BURN", required, additional),
                new ClaimTypeDTO("SINK", required, additional));
    }

    @Override
    public void changeType(String id, ClaimTypeDTO dto) throws ClaimsServiceException, ClaimNotFoundException {
        ClaimDTO claim = find(id);
        claim.setType(dto);
    }

}
