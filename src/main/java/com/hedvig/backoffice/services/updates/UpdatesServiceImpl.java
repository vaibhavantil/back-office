package com.hedvig.backoffice.services.updates;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.Updates;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.repository.UpdatesRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Service
public class UpdatesServiceImpl implements UpdatesService {

    private final UpdatesRepository updatesRepository;
    private final PersonnelRepository personnelRepository;
    private final SimpMessagingTemplate template;

    private CopyOnWriteArraySet<String> subscriptions;

    public UpdatesServiceImpl(UpdatesRepository updatesRepository,
                              PersonnelRepository personnelRepository,
                              SimpMessagingTemplate template) {

        this.updatesRepository = updatesRepository;
        this.personnelRepository = personnelRepository;
        this.template = template;

        this.subscriptions = new CopyOnWriteArraySet<>();
    }

    @Override
    @Transactional
    public void append(int count, UpdateType type) {
        List<Updates> updates = updatesRepository.findByType(type);
        updates.forEach(u -> u.setCount(u.getCount() + count));
        updatesRepository.save(updates);
    }

    @Override
    @Transactional
    public void subscribe(String email) {
        Optional<Personnel> optional = personnelRepository.findByEmail(email);
        optional.ifPresent(p -> {
            subscriptions.add(email);
            List<Updates> updates = updatesRepository.findByPersonnel(p);

            if (updates.size() == 0) {
                updates = Arrays
                        .stream(UpdateType.values())
                        .map(t -> new Updates(t, p, 0))
                        .collect(Collectors.toList());

                updatesRepository.save(updates);
            }
        });
    }

    @Override
    public void unsubscribe(String email) {
        Optional<Personnel> optional = personnelRepository.findByEmail(email);
        optional.ifPresent(p -> subscriptions.remove(email));
    }

    @Override
    public void send() {

    }

}
