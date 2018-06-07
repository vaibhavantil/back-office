package com.hedvig.backoffice.services.claims;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.config.feign.ExternalServiceNotFoundException;
import com.hedvig.backoffice.services.claims.dto.*;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.settings.SystemSettingsService;
import com.hedvig.backoffice.web.dto.MemberDTO;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClaimsServiceStub implements ClaimsService {

    private static Logger logger = LoggerFactory.getLogger(ClaimsServiceStub.class);

    private List<Claim> claims;
    private List<ClaimType> types;

    public ClaimsServiceStub(MemberService memberService, SystemSettingsService settingsService) {
        try {
            val resource = new ClassPathResource("claim_types.json").getInputStream();
            val mapper = new ObjectMapper();
            types = mapper.readValue(
                    resource,
                    mapper.getTypeFactory().constructCollectionType(List.class, ClaimType.class));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        List<String> memberIds = memberService.listAllMembers(0,0,null,null,settingsService.getInternalAccessToken())
            .getContent().stream().map(o -> o.getMemberId().toString()).collect(Collectors.toList());

        claims = IntStream.range(0, 10).mapToObj(i -> {
            String id = UUID.randomUUID().toString();
            String memberId = memberIds.size() > i
                    ? memberIds.get(i)
                    : memberIds.size() > 0 ? memberIds.get(0) : UUID.randomUUID().toString();

            Claim claim = new Claim();
            claim.setId(id);
            claim.setUserId(memberId);
            claim.setState(ClaimState.OPEN);
            claim.setAudioURL("http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg");
            claim.setPayments(new ArrayList<>());
            claim.setNotes(new ArrayList<>());
            claim.setEvents(new ArrayList<>());
            claim.setData(new ArrayList<>());
            claim.setAssets(new ArrayList<>());
            claim.setDate(LocalDateTime.now());
            claim.setRegistrationDate(LocalDateTime.now());

            return claim;
        }).collect(Collectors.toList());

        logger.info("CLAIMS SERVICE:");
        logger.info("class: " + ClaimsServiceStub.class.getName());
    }

    @Override
    public List<Claim> list(String token) {
        return claims.stream()
                .map(c -> {
                    Claim claim = new Claim();
                    claim.setId(c.getId());
                    claim.setUserId(c.getUserId());
                    claim.setState(c.getState());
                    claim.setReserve(c.getReserve());
                    claim.setType(c.getType());
                    claim.setDate(c.getDate());
                    claim.setAudioURL(c.getAudioURL());
                    claim.setRegistrationDate(c.getRegistrationDate());

                    return claim;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Claim> listByUserId(String userId, String token) {
        return claims.stream().filter(c -> c.getUserId().equals(userId)).collect(Collectors.toList());
    }

    @Override
    public Claim find(String id, String token) {
        return claims.stream().filter(c -> c.getId().equals(id)).findAny()
                .orElseThrow(() -> new ExternalServiceNotFoundException("claim not found", "mock"));
    }

    @Override
    public List<ClaimType> types(String token) {
        return types;
    }

    @Override
    public void addPayment(ClaimPayment dto, String token) {
        Claim claim = find(dto.getClaimID(), token);
        claim.getPayments().add(dto);
        addEvent(claim,"[test] payment added");
    }

    @Override
    public void addNote(ClaimNote dto, String token) {
        Claim claim = find(dto.getClaimID(), token);
        claim.getNotes().add(dto);
        addEvent(claim,"[test] note added");
    }

    @Override
    public void addData(ClaimData data, String token) {
        Claim claim = find(data.getClaimID(), token);
        claim.getData().add(data);
        addEvent(claim,"[test] data added");
    }

    @Override
    public void changeState(ClaimStateUpdate state, String token) {
        Claim claim = find(state.getClaimID(), token);
        claim.setState(state.getState());
        addEvent(claim,"[test] state changed");
    }

    @Override
    public void changeReserve(ClaimReserveUpdate reserve, String token) {
        Claim claim = find(reserve.getClaimID(), token);
        claim.setReserve(reserve.getAmount());
        addEvent(claim,"[test] reserve changed");
    }

    @Override
    public void changeType(ClaimTypeUpdate type, String token) {
        Claim claim = find(type.getClaimID(), token);
        claim.setType(type.getType());
        addEvent(claim,"[test] type changed");
    }

    @Override
    public Map<String, Long> statistics(String token) {
        Map<String, Long> stat = new HashMap<>();
        for (ClaimState state : ClaimState.values()) {
            stat.put(state.name(), 10L);
        }

        return stat;
    }

    @Override
    public long totalClaims(String token) {
        val stat = statistics(token);

        return stat.getOrDefault(ClaimState.OPEN.name(), 0L)
                + stat.getOrDefault(ClaimState.REOPENED.name(), 0L);
    }

    private void addEvent(Claim claim, String message) {
        ClaimEvent event = new ClaimEvent();
        event.setText(message);
        event.setDate(LocalDateTime.now());

        claim.getEvents().add(event);
    }
}
