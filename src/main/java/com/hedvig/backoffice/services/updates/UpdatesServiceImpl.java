package com.hedvig.backoffice.services.updates;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.UpdateContext;
import com.hedvig.backoffice.domain.Updates;
import com.hedvig.backoffice.repository.*;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.settings.SystemSettingsService;
import com.hedvig.backoffice.services.updates.data.UpdatesDTO;
import com.hedvig.common.constant.AssetState;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UpdatesServiceImpl implements UpdatesService {

    private final UpdatesRepository updatesRepository;
    private final PersonnelRepository personnelRepository;
    private final AssetRepository assetRepository;
    private final QuestionGroupRepository questionRepository;
    private final ClaimsService claimsService;
    private final SystemSettingsService settingsService;
    private final UpdateContextRepository updateContextRepository;

    private final SimpMessagingTemplate template;

    public UpdatesServiceImpl(UpdatesRepository updatesRepository,
                              PersonnelRepository personnelRepository,
                              AssetRepository assetRepository,
                              QuestionGroupRepository questionRepository,
                              ClaimsService claimsService,
                              SystemSettingsService settingsService,
                              SimpMessagingTemplate template,
                              UpdateContextRepository updateContextRepository) {

        this.updatesRepository = updatesRepository;
        this.personnelRepository = personnelRepository;
        this.assetRepository = assetRepository;
        this.questionRepository = questionRepository;
        this.claimsService = claimsService;
        this.settingsService = settingsService;
        this.updateContextRepository = updateContextRepository;

        this.template = template;
    }

    @Override
    @Transactional
    public void changeOn(long count, UpdateType type) {
        if (count != 0) {
            List<Updates> updates = updatesRepository.findByType(type);
            updates.forEach(u -> u.setCount(u.getCount() + count));
            updatesRepository.save(updates);

            sendForActiveContext(type);
        }
    }

    @Override
    @Transactional
    public void set(long count, UpdateType type) {
        List<Updates> updates = updatesRepository.findByType(type);
        updates.forEach(u -> u.setCount(count));
        updatesRepository.save(updates);

        sendForActiveContext(type);
    }

    @Override
    @Transactional
    public void init(String personnelId) throws AuthorizationException {
        Personnel personnel = personnelRepository.findById(personnelId).orElseThrow(AuthorizationException::new);

        UpdateContext uc = updateContextRepository
                .findByPersonnel(personnel)
                .orElseGet(() -> {
                    UpdateContext context = new UpdateContext(personnel);
                    updateContextRepository.save(context);
                    return context;
                });

        updatesRepository.deleteByContext(uc);

        List<Updates> updates = new ArrayList<>();
        for (UpdateType type : UpdateType.values()) {
            switch (type) {
                case QUESTIONS:
                    updates.add(new Updates(UpdateType.QUESTIONS, uc,
                            Optional.ofNullable(questionRepository.notAnsweredCount()).orElse(0L)));
                    break;
                case ASSETS:
                    updates.add(new Updates(UpdateType.ASSETS, uc, assetRepository.countAllByState(AssetState.PENDING)));
                    break;
                case CLAIMS:
                    updates.add(new Updates(UpdateType.CLAIMS, uc, claimsService.totalClaims(settingsService.getInternalAccessToken())));
                    break;
                default:
                    updates.add(new Updates(type, uc,0L));
                    break;
            }
        }

        updatesRepository.save(updates);
    }

    @Override
    @Transactional
    public void updates(String personnelId) {
        Optional<UpdateContext> uc = updateContextRepository.findByPersonnelId(personnelId);

        uc.ifPresent(context -> {
            List<Updates> updates = updatesRepository.findByContext(context);
            send(context.getPersonnel(), updates);
        });
    }

    @Override
    @Transactional
    public void subscribe(String personnelId, String sessionId, String subId) throws AuthorizationException {
        Personnel personnel = personnelRepository.findById(personnelId).orElseThrow(AuthorizationException::new);

        UpdateContext uc = updateContextRepository
                .findByPersonnel(personnel)
                .orElseGet(() -> new UpdateContext(personnel));

        uc.setActive(true);
        uc.setSessionId(sessionId);
        uc.setSubId(subId);

        updateContextRepository.save(uc);
    }

    @Override
    @Transactional
    public void unsubscribe(String personnelId, String sessionId, String subId) {
        Optional<UpdateContext> optional = updateContextRepository
                .findByPersonnelIdAndSessionIdAndSubId(personnelId, sessionId, subId);

        if (optional.isPresent()) {
            UpdateContext uc = optional.get();
            uc.setActive(false);
            updateContextRepository.save(uc);
        }
    }

    @Override
    public void close(String sessionId) {
        Optional<UpdateContext> uc = updateContextRepository.findBySessionId(sessionId);
        uc.ifPresent(context -> {
            context.setActive(false);
            updateContextRepository.save(context);
        });
    }

    private void sendForActiveContext(UpdateType type) {
        List<Updates> updates = updatesRepository.findByTypeAndActiveContext(type);
        updates.forEach(u -> send(u.getContext().getPersonnel(), Collections.singletonList(u)));
    }

    private void send(Personnel personnel, List<Updates> updates) {
        List<UpdatesDTO> dto = updates.stream()
                .map(UpdatesDTO::fromDomain)
                .collect(Collectors.toList());

        template.convertAndSendToUser(personnel.getId(), "/updates", dto);
    }

}
