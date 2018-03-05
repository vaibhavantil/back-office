package com.hedvig.backoffice.services.assettracker;

import com.hedvig.backoffice.domain.Asset;
import com.hedvig.backoffice.repository.AssetRepository;
import com.hedvig.backoffice.services.updates.UpdateType;
import com.hedvig.backoffice.services.updates.UpdatesService;
import com.hedvig.backoffice.web.dto.assets.AssetDTO;
import com.hedvig.common.constant.AssetState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetTrackerServiceImpl implements AssetTrackerService {

    private static Logger logger = LoggerFactory.getLogger(AssetTrackerServiceImpl.class);

    private final AssetRepository assetRepository;
    private final AssetTracker tracker;
    private final UpdatesService updatesService;

    @Autowired
    public AssetTrackerServiceImpl(AssetRepository assetRepository, AssetTracker tracker, UpdatesService updatesService) {
        this.assetRepository = assetRepository;
        this.tracker = tracker;
        this.updatesService = updatesService;
    }

    @PostConstruct
    @Transactional
    public void setup() {
        assetRepository.deleteAll();
    }

    @Transactional
    @Override
    public void loadPendingAssetsFromTracker() {
        List<Asset> assets = tracker.findPendingAssets();
        if (assets.size() > 0) {
            List<String> ids = assets
                    .stream()
                    .map(Asset::getId)
                    .collect(Collectors.toList());

            List<Asset> exists = assetRepository.findAssetsById(ids);
            updatesService.change(assets.size() - exists.size(), UpdateType.ASSETS);

            assetRepository.save(assets);
            logger.info("Pending assets added");
        }
    }

    @Transactional
    @Override
    public List<AssetDTO> findAll() {
        return assetRepository.streamAll()
                .map(AssetDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public AssetDTO find(String assetId) throws AssetNotFoundException {
        Asset asset = assetRepository.findOne(assetId);
        if (asset == null) {
            throw new AssetNotFoundException(String.format("asset with id %s not found", assetId));
        }

        return AssetDTO.fromDomain(asset);
    }

    @Transactional
    @Override
    public void changeAssetState(String assetId, AssetState state) throws AssetNotFoundException, AssetTrackerException {
        Asset asset = assetRepository.findOne(assetId);
        if (asset != null) {
            asset.setState(state);
            tracker.updateAsset(asset);
            assetRepository.save(asset);
            logger.info(String.format("state for asset with id %s changed to %s", assetId, state.name()));
        } else {
            throw new AssetNotFoundException(String.format("asset with id %s not found", assetId));
        }
    }
}
