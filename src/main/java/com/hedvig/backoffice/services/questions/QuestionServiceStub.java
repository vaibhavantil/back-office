package com.hedvig.backoffice.services.questions;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.messages.BotMessageException;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.messages.BotServiceStub;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.questions.dto.Question;
import com.hedvig.backoffice.web.dto.MemberDTO;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionServiceStub implements QuestionService {

    private final BotService botService;
    private final MemberService memberService;
    private final Personnel personnel;

    private List<Question> questions;

    private static final String STUB_MESSAGE_TEMPLATE = "{" +
            "\"header\": { " +
            "   \"fromId\": %s" +
            "}," +
            "\"body\": {" +
            "   \"type\": \"text\"," +
            "   \"text\": \"Test question\"" +
            "}," +
            "\"timestamp\":\"%s\"" +
            "}";

    public QuestionServiceStub(MemberService memberService, PersonnelRepository personnelRepository, BotService botService) {
        this.botService = botService;
        this.questions = new ArrayList<>();
        this.memberService = memberService;
        this.personnel = personnelRepository.findByEmail("admin@hedvig.com").orElseGet(Personnel::new);
    }

    @Override
    public List<Question> list() {
        if (questions.size() == 0) {
            List<MemberDTO> members = memberService.search("", "").orElseGet(ArrayList::new);
            for (MemberDTO m : members) {
                String messageStr = String.format(STUB_MESSAGE_TEMPLATE, m.getHid(), Instant.now().toString());
                BotMessage message;
                try {
                    message = new BotMessage(messageStr, true);
                } catch (BotMessageException e) {
                    throw new RuntimeException();
                }

                try {
                    botService.response(m.getHid(), message);
                } catch (BotServiceException e) {
                    throw new RuntimeException();
                }
                Question q = new Question(UUID.randomUUID().toString(),
                        message.getMessage(),
                        null,
                        PersonnelDTO.fromDomain(personnel),
                        m,
                        LocalDateTime.now());

                questions.add(q);
            }
        }

        return questions;
    }

    @Override
    public boolean answer(String id, BotMessage message) throws BotServiceException {
        Optional<Question> optional = questions.stream().filter(q -> q.getId().equals(id)).findAny();
        if (optional.isPresent()) {
            Question q = optional.get();
            q.setAnswer(message.getMessage());
            botService.response(q.getMember().getHid(), message);
            return true;
        }

        return false;
    }
}
