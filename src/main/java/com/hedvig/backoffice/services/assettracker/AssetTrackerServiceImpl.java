package com.hedvig.backoffice.services.assettracker;

import com.hedvig.backoffice.domain.Asset;
import com.hedvig.backoffice.repository.AssetRepository;
import com.hedvig.backoffice.services.updates.UpdateType;
import com.hedvig.backoffice.services.updates.UpdatesService;
import com.hedvig.backoffice.web.dto.assets.AssetDTO;
import com.hedvig.common.constant.AssetState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AssetTrackerServiceImpl implements AssetTrackerService {

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
        final List<Asset> importedAssets = tracker.findPendingAssets();
        if (importedAssets.size() > 0) {
            final List<String> importedIds = importedAssets
                    .stream()
                    .map(Asset::getId)
                    .collect(Collectors.toList());
            final Map<String, Asset> existingAssets = assetRepository
                    .findAssetsById(importedIds)
                    .stream()
                    .collect(Collectors.toMap(Asset::getId, a -> a));

            final List<Asset> changedAssets = importedAssets
                    .stream()
                    .filter(a -> !a.equals(existingAssets.get(a.getId())))
                    .collect(Collectors.toList());

            if (changedAssets.size() > 0) {
                assetRepository.save(changedAssets);
                final Long pendingCount = assetRepository.countAllByState(AssetState.PENDING);
                updatesService.change(pendingCount, UpdateType.ASSETS);
                log.info("Synchronized assets, added/updated {} assets, new pending count {}", changedAssets.size(), pendingCount);
            }
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
            log.info("state for asset with id {} changed to {}", assetId, state.name());
        } else {
            throw new AssetNotFoundException(String.format("asset with id %s not found", assetId));
        }
    }
}
