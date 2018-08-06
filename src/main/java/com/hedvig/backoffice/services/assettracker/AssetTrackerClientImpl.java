package com.hedvig.backoffice.services.assettracker;

import com.hedvig.backoffice.domain.Asset;
import com.hedvig.backoffice.web.dto.assets.AssetDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AssetTrackerClientImpl implements AssetTrackerClient {

  private static Logger logger = LoggerFactory.getLogger(AssetTrackerClientImpl.class);

  private String trackerUrl;
  private String assetsPath;
  private String updatePath;

  @Autowired
  public AssetTrackerClientImpl(
      @Value("${tracker.baseUrl}") String trackerUrl,
      @Value("${tracker.urls.assets}") String assetsPath,
      @Value("${tracker.urls.update}") String updatePath) {
    this.trackerUrl = trackerUrl;
    this.assetsPath = assetsPath;
    this.updatePath = updatePath;

    logger.info("ASSET TRACKER SERVICE:");
    logger.info("class: " + AssetTrackerClientImpl.class.getName());
    logger.info("base: " + trackerUrl);
    logger.info("assets: " + assetsPath);
  }

  @Override
  public List<Asset> findPendingAssets() {
    RestTemplate template = new RestTemplate();
    try {
      ResponseEntity<AssetDTO[]> response =
          template.getForEntity(trackerUrl + assetsPath, AssetDTO[].class);
      if (response.getStatusCode() == HttpStatus.OK) {
        return Arrays.stream(response.getBody())
            .map(AssetDTO::toDomain)
            .collect(Collectors.toList());
      } else {
        logger.error("Assets are not fetched from tracker");
      }
    } catch (Exception e) {
      logger.error("Assets are not fetched from tracker", e);
    }

    return new ArrayList<>();
  }

  @Override
  public void updateAsset(Asset asset) throws AssetTrackerException {
    RestTemplate template = new RestTemplate();
    try {
      template.put(trackerUrl + updatePath + "/" + asset.getId(), asset);
    } catch (Exception e) {
      throw new AssetTrackerException(e);
    }
  }
}
