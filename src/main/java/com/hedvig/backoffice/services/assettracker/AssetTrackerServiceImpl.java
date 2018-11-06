package com.hedvig.backoffice.services.assettracker;

import com.hedvig.backoffice.domain.Asset;
import com.hedvig.backoffice.repository.AssetRepository;
import com.hedvig.backoffice.services.updates.UpdateType;
import com.hedvig.backoffice.services.updates.UpdatesService;
import com.hedvig.backoffice.web.dto.assets.AssetDTO;
import com.hedvig.common.constant.AssetState;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AssetTrackerServiceImpl implements AssetTrackerService {

  private final AssetRepository assetRepository;

  private final AssetTrackerClient trackerClient;

  private final UpdatesService updatesService;

  private boolean enabled;

  @Autowired
  public AssetTrackerServiceImpl(
      AssetRepository assetRepository,
      AssetTrackerClient trackerClient,
      UpdatesService updatesService,
      @Value("${tracker.enabled:false}") boolean enabled) {
    this.assetRepository = assetRepository;
    this.trackerClient = trackerClient;
    this.updatesService = updatesService;
    this.enabled = enabled;
  }

  @Transactional
  @Override
  public void loadPendingAssetsFromTracker() {
    if (!enabled) return;

    final List<Asset> importedAssets = trackerClient.findPendingAssets();
    if (importedAssets.size() > 0) {
      final List<String> importedIds =
          importedAssets.stream().map(Asset::getId).collect(Collectors.toList());
      final Map<String, Asset> existingAssets =
          assetRepository
              .findAssetsById(importedIds)
              .stream()
              .collect(Collectors.toMap(Asset::getId, a -> a));

      final List<Asset> changedAssets =
          importedAssets
              .stream()
              .filter(a -> !a.equals(existingAssets.get(a.getId())))
              .collect(Collectors.toList());

      if (changedAssets.size() > 0) {
        assetRepository.saveAll(changedAssets);
        final Long pendingCount = assetRepository.countAllByState(AssetState.PENDING);
        updatesService.set(pendingCount, UpdateType.ASSETS);
        log.info(
            "Synchronized assets, added/updated {} assets, new pending count {}",
            changedAssets.size(),
            pendingCount);
      }
    }
  }

  @Transactional
  @Override
  public List<AssetDTO> findAll() {
    return assetRepository.streamAll().map(AssetDTO::fromDomain).collect(Collectors.toList());
  }

  @Transactional
  @Override
  public AssetDTO find(String assetId) throws AssetNotFoundException {
    val asset = assetRepository.findById(assetId);

    return asset
        .map(AssetDTO::fromDomain)
        .orElseThrow(
            () -> {

              //noinspection ThrowableNotThrown
              return new AssetNotFoundException(
                  String.format("asset with id %s not found", assetId));
            });
  }

  @Transactional
  @Override
  public void changeAssetState(String assetId, AssetState state)
      throws AssetNotFoundException, AssetTrackerException {
    val assetMaybe = assetRepository.findById(assetId);

    if (assetMaybe.isPresent()) {
      val asset = assetMaybe.get();

      asset.setState(state);
      trackerClient.updateAsset(asset);
      assetRepository.save(asset);
      final Long pendingCount = assetRepository.countAllByState(AssetState.PENDING);
      updatesService.set(pendingCount, UpdateType.ASSETS);
      log.info("state for asset with id {} changed to {}", assetId, state.name());
    } else {
      throw new AssetNotFoundException(String.format("asset with id %s not found", assetId));
    }
  }
}
