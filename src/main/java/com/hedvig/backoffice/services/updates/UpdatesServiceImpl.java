package com.hedvig.backoffice.services.updates;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.Updates;
import com.hedvig.backoffice.repository.AssetRepository;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.repository.QuestionRepository;
import com.hedvig.backoffice.repository.UpdatesRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.updates.data.UpdatesDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UpdatesServiceImpl implements UpdatesService {

    private final UpdatesRepository updatesRepository;
    private final PersonnelRepository personnelRepository;
    private final AssetRepository assetRepository;
    private final QuestionRepository questionRepository;
    private final ClaimsService claimsService;

    private final SimpMessagingTemplate template;

    public UpdatesServiceImpl(UpdatesRepository updatesRepository,
                              PersonnelRepository personnelRepository,
                              AssetRepository assetRepository,
                              QuestionRepository questionRepository,
                              ClaimsService claimsService,
                              SimpMessagingTemplate template) {

        this.updatesRepository = updatesRepository;
        this.personnelRepository = personnelRepository;
        this.assetRepository = assetRepository;
        this.questionRepository = questionRepository;
        this.claimsService = claimsService;

        this.template = template;
    }

    @Override
    @Transactional
    public void change(long count, UpdateType type) {
        if (count > 0) {
            List<Updates> updates = updatesRepository.findByType(type);
            updates.forEach(u -> u.setCount(u.getCount() + count));
            updatesRepository.save(updates);

            updates.forEach(u -> send(u.getPersonnel(), Collections.singletonList(u)));
        }
    }

    @Override
    public void set(long count, UpdateType type) {
        List<Updates> updates = updatesRepository.findByType(type);
        updates.forEach(u -> u.setCount(count));
        updatesRepository.save(updates);

        updates.forEach(u -> send(u.getPersonnel(), Collections.singletonList(u)));
    }


    @Override
    @Transactional
    public void init(String email) throws AuthorizationException {
        Personnel personnel = personnelRepository.findByEmail(email).orElseThrow(AuthorizationException::new);
        List<Updates> updates = updatesRepository.findByPersonnel(personnel);
        if (updates.size() == 0) {
            updates.add(new Updates(UpdateType.ASSETS, personnel, assetRepository.count()));
            updates.add(new Updates(UpdateType.QUESTIONS, personnel,
                    Optional.ofNullable(questionRepository.notAnsweredCount()).orElse(0L)));
            updates.add(new Updates(UpdateType.CLAIMS, personnel, (long) claimsService.list().size()));

            updatesRepository.save(updates);
        }
    }

    @Override
    @Transactional
    public void clear(String email, UpdateType type) {
        Optional<Personnel> optional = personnelRepository.findByEmail(email);
        optional.ifPresent(p -> {
            List<Updates> updates = updatesRepository.findByPersonnelAndType(p, type);
            updates.forEach(u -> u.setCount(0L));
            updatesRepository.save(updates);
        });
    }

    @Override
    @Transactional
    public void updates(String email) {
        Optional<Personnel> optional = personnelRepository.findByEmail(email);
        optional.ifPresent(p -> {
            List<Updates> updates = updatesRepository.findByPersonnel(p);
            send(p, updates);
        });
    }

    private void send(Personnel personnel, List<Updates> updates) {
        List<UpdatesDTO> dto = updates.stream()
                .map(UpdatesDTO::fromDomain)
                .collect(Collectors.toList());

        template.convertAndSendToUser(personnel.getEmail(), "/updates", dto);
    }

}
