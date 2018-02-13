package com.hedvig.backoffice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.MemberServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class BackOfficeStartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(BackOfficeStartupApplicationListener.class);

    private final MemberService memberService;
    private boolean memberServiceStub;

    @Autowired
    public BackOfficeStartupApplicationListener(MemberService memberService,
                                                @Value("${memberservice.stub:false}") boolean memberServiceStub) {
        this.memberService = memberService;
        this.memberServiceStub = memberServiceStub;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!memberServiceStub) {
            try {
                logger.info("TEST MEMBER SERVICE:");
                ObjectMapper mapper = new ObjectMapper();
                memberService.list()
                        .stream()
                        .limit(10)
                        .forEach(u -> {
                            try {
                                logger.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(u));
                            } catch (JsonProcessingException e) {
                                logger.error("error during parsing members from service", e);
                            }
                        });
            } catch (MemberServiceException e) {
                logger.error("error during updating members list", e);
            }
        }
    }

}
