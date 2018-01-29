package com.hedvig.backoffice.services.updates;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.Updates;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.repository.UpdatesRepository;
import com.hedvig.backoffice.services.updates.data.UpdatesDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        if (count > 0) {
            List<Updates> updates = updatesRepository.findByType(type);
            updates.forEach(u -> u.setCount(u.getCount() + count));
            updatesRepository.save(updates);

            Map<Personnel, List<Updates>> updatesByPersonnel = updates.stream()
                    .filter(u -> subscriptions.contains(u.getPersonnel().getEmail()))
                    .collect(Collectors.groupingBy(Updates::getPersonnel));

            updatesByPersonnel.forEach(this::send);
        }
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
    @Transactional
    public void clear(String email, UpdateType type) {
        Optional<Personnel> optional = personnelRepository.findByEmail(email);
        optional.ifPresent(p -> {
            List<Updates> updates = updatesRepository.findByPersonnelAndType(p, type);
            updates.forEach(u -> u.setCount(0));
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
