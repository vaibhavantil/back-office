package com.hedvig.backoffice.services.assettracker;

import com.hedvig.backoffice.domain.Asset;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.MemberServiceException;
import com.hedvig.backoffice.web.dto.MemberDTO;
import com.hedvig.common.constant.AssetState;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AssetTrackerStub implements AssetTracker {

    private static Logger logger = LoggerFactory.getLogger(AssetTrackerStub.class);

    private AtomicInteger generation;
    private AtomicInteger globalId;

    private final MemberService memberService;

    @Autowired
    public AssetTrackerStub(MemberService memberService) {
        this.memberService = memberService;

        logger.info("ASSET TRACKER SERVICE:");
        logger.info("class: " + AssetTrackerStub.class.getName());
        generation = new AtomicInteger(0);
        globalId = new AtomicInteger(0);
    }

    @Override
    public List<Asset> findPendingAssets() {
        if (generation.get() < 10) {
            logger.info("fetch pending assets");
            generation.incrementAndGet();
            List<MemberDTO> members = null;
            try {
                 members = memberService.list();
            } catch (MemberServiceException e) {
                logger.error("error during fetch members", e);
            }

            List<String> memberIds = members != null
                    ? members.stream().map(MemberDTO::getHid).collect(Collectors.toList())
                    : IntStream.range(0, 15).mapToObj(i -> "user-id-" + i).collect(Collectors.toList());

            return IntStream.range(0, 15).mapToObj(i -> {
                val id = UUID.randomUUID().toString();
                String userId = memberIds.size() > i ? memberIds.get(i) : memberIds.get(0);

                return new Asset(
                        id,
                        "http://78.media.tumblr.com/tumblr_ll313eVnI91qjahcpo1_1280.jpg",
                        "http://thecatapi.com/?id=3hn",
                        "Asset number " + globalId.incrementAndGet(),
                        AssetState.PENDING,
                        i % 2 == 0,
                        userId,
                        LocalDate.now()
                );
            }).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public void updateAsset(Asset asset) throws AssetTrackerException {

    }

}
