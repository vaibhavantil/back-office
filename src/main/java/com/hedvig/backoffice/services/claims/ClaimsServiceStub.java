package com.hedvig.backoffice.services.claims;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.services.users.UserService;
import com.hedvig.backoffice.services.users.UserServiceException;
import com.hedvig.backoffice.web.dto.UserDTO;
import com.hedvig.backoffice.web.dto.claims.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ClaimsServiceStub implements ClaimsService {

    private List<ClaimDTO> claims;
    private List<ClaimTypeDTO> types;
    private Map<String, List<ClaimEventDTO>> events;
    private Map<String, List<ClaimPayoutDTO>> payments;
    private Map<String, List<ClaimNoteDTO>> notes;

    @Autowired
    public ClaimsServiceStub(UserService userService) throws ClaimException, UserServiceException {
        events = new HashMap<>();
        payments = new HashMap<>();
        notes = new HashMap<>();

        try {
            val resource = new ClassPathResource("claim_types.json").getInputStream();
            val mapper = new ObjectMapper();
            types = mapper.readValue(
                    resource,
                    mapper.getTypeFactory().constructCollectionType(List.class, ClaimTypeDTO.class));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        List<UserDTO> users = userService.list();

        claims = IntStream.range(0, 10).mapToObj(i -> {
            String id = UUID.randomUUID().toString();
            UserDTO user = users.size() < i ? users.get(i) : users.get(users.size() - 1);

            return new ClaimDTO(id,
                    user.getHid(),
                    ClaimStatus.OPEN,
                    types.get(0),
                    "http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg",
                    new BigDecimal(0),
                    new BigDecimal(0),
                    new Date().toInstant());
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

    @Override
    public List<ClaimPayoutDTO> payouts(String id) throws ClaimException {
        find(id);
        return Optional.ofNullable(payments.get(id))
                .orElse(new ArrayList<>());
    }

    @Override
    public ClaimPayoutDTO addPayout(ClaimPayoutDTO dto) throws ClaimException {
        dto.setId(UUID.randomUUID().toString());

        ClaimDTO claim = find(dto.getClaimId());
        ClaimNoteDTO note = notes(dto.getClaimId())
                .stream()
                .filter(n -> n.getId().equals(dto.getNoteId()))
                .findAny()
                .orElseThrow(() -> new ClaimNotFoundException("note with id " + dto.getNoteId() + " not found"));

        List<ClaimPayoutDTO> list = payments.computeIfAbsent(dto.getClaimId(), k -> new ArrayList<>());
        list.add(dto);

        BigDecimal total = list.stream()
                .map(ClaimPayoutDTO::getAmount)
                .reduce(new BigDecimal(0), BigDecimal::add);

        claim.setTotal(total);
        save(claim);

        ClaimEventDTO event = new ClaimEventDTO(dto.getClaimId(),
                "new payout: amount = " + dto.getAmount().toString() + ", total = " + claim.getTotal().toString());
        addEvent(event);

        return dto;
    }

    @Override
    public void removePayout(String id, String claimId) throws ClaimException {
        ClaimDTO claim = find(claimId);

        List<ClaimPayoutDTO> list = payments.computeIfAbsent(claimId, k -> new ArrayList<>());
        int index = IntStream.range(0, list.size())
                .filter(i -> list.get(i).getId().equals(id))
                .findFirst().orElseThrow(() -> new ClaimNotFoundException("payment with id " + id + " not found"));

        list.remove(index);

        BigDecimal total = list.stream()
                .map(ClaimPayoutDTO::getAmount)
                .reduce(new BigDecimal(0), BigDecimal::add);

        claim.setTotal(total);
        save(claim);

        ClaimEventDTO event = new ClaimEventDTO(claimId,
                "delete payout: total = " + claim.getTotal().toString());
        addEvent(event);
    }

    @Override
    public List<ClaimNoteDTO> notes(String id) throws ClaimException {
        find(id);
        return Optional.ofNullable(notes.get(id))
                .orElse(new ArrayList<>());
    }

    @Override
    public ClaimNoteDTO addNote(ClaimNoteDTO dto) throws ClaimException {
        dto.setId(UUID.randomUUID().toString());

        List<ClaimNoteDTO> list = notes.computeIfAbsent(dto.getClaimId(), k -> new ArrayList<>());
        list.add(dto);

        return dto;
    }

    @Override
    public void removeNote(String id, String claimId) throws ClaimException {
        find(claimId);
        List<ClaimNoteDTO> list = notes.computeIfAbsent(claimId, k -> new ArrayList<>());
        int index = IntStream.range(0, list.size())
                .filter(i -> list.get(i).getId().equals(id))
                .findFirst().orElseThrow(() -> new ClaimNotFoundException("note with id " + id + " not found"));

        list.remove(index);
    }

    @Override
    public void changeType(String id, ClaimTypeDTO dto) throws ClaimException {
        ClaimDTO claim = find(id);
        claim.setType(dto);
        save(claim);

        ClaimEventDTO event = new ClaimEventDTO(id, "type changed to " + dto.getName());
        addEvent(event);
    }

    @Override
    public void changeStatus(String id, ClaimStatus status) throws ClaimException {
        ClaimDTO claim = find(id);
        claim.setStatus(status);
        save(claim);

        ClaimEventDTO event = new ClaimEventDTO(id, "status changed to " + status.toString());
        addEvent(event);
    }

    @Override
    public void setResume(String id, BigDecimal resume) throws ClaimException {
        ClaimDTO claim = find(id);
        claim.setResume(resume);
        save(claim);

        ClaimEventDTO event = new ClaimEventDTO(id, "resume changed to " + resume.toString());
        addEvent(event);
    }

}
