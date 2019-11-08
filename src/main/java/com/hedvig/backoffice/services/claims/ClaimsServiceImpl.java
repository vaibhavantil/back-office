package com.hedvig.backoffice.services.claims;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.hedvig.backoffice.services.claims.dto.*;
import feign.FeignException;
import java.io.IOException;
import java.util.ArrayList;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class ClaimsServiceImpl implements ClaimsService {

  private final ClaimsServiceClient client;
  private final AmazonS3 amazonS3;
  private final String bucketName;

  @Autowired
  public ClaimsServiceImpl(ClaimsServiceClient client, AmazonS3 amazonS3, @Value("${claims.bucketName}") String bucketName) {
    this.client = client;
    this.amazonS3 = amazonS3;
    this.bucketName = bucketName;
  }

  @Override
  public List<Claim> list(String token) {
    return client.list(token);
  }

  @Override
  public List<Claim> listByUserId(String userId, String token) {
    return client.listByUserId(userId, token);
  }

  @Override
  public Claim find(String id, String token) {
    return client.find(id, token);
  }

  @Override
  public List<ClaimType> types(String token) {
    return client.types(token);
  }

  @Override
  public ClaimSearchResultDTO search(Integer page, Integer pageSize, ClaimSortColumn sortBy,
                                     Sort.Direction sortDirection, String token) {
    return client.search(page, pageSize, sortBy, sortDirection, token);
  }

  @Override
  public ClaimPaymentResponse addPayment(String memberId, ClaimPayment dto, String token) {
    switch (dto.getType()) {
      case Manual: {
        try {
          client.addPayment(dto, token);
          return ClaimPaymentResponse.SUCCESSFUL;
        } catch (FeignException ex) {
          if (ex.status() == HttpStatus.FORBIDDEN.value()) {
            return ClaimPaymentResponse.FORBIDDEN;
          }
          return ClaimPaymentResponse.FAILED;
        }
      }

      case Automatic: {
        try {
          client.addAutomaticPayment(memberId,
            ClaimPaymentRequest.fromClaimPayment(dto));
          return ClaimPaymentResponse.SUCCESSFUL;
        } catch (FeignException ex) {
          if (ex.status() == HttpStatus.FORBIDDEN.value()) {
            return ClaimPaymentResponse.FORBIDDEN;
          }
          return ClaimPaymentResponse.FAILED;
        }
      }

      default:
        throw new RuntimeException(
          String.format("Unhandled Claim Payment Type: %s", dto.getType()));
    }
  }

  @Override
  public void addNote(ClaimNote dto, String token) {
    client.addNote(dto, token);
  }

  @Override
  public void addData(ClaimData data, String token) {
    client.addDataItem(data, token);
  }

  @Override
  public void changeState(ClaimStateUpdate state, String token) {
    client.updateState(state, token);
  }

  @Override
  public void changeReserve(ClaimReserveUpdate reserve, String token) {
    client.updateReserve(reserve, token);
  }

  @Override
  public void changeType(ClaimTypeUpdate type, String token) {
    client.updateType(type, token);
  }

  @Override
  public Map<String, Long> statistics(String token) {
    return client.statistics(token);
  }

  @Override
  public long totalClaims(String token) {
    val stat = statistics(token);

    return stat.getOrDefault(ClaimState.OPEN.name(), 0L)
      + stat.getOrDefault(ClaimState.REOPENED.name(), 0L);
  }

  @Override
  public List<Claim> getClaimsByIds(List<UUID> ids) {
    return client.getClaimsByIds(new ClaimsByIdsDto(ids))
      .stream()
      .map(c -> {
        val signedAudioUrl = signAudioUrl(c.getAudioURL());
        val claimWithSigned = c.toBuilder().audioURL(signedAudioUrl).build();
        claimWithSigned.setId(c.getId());
        claimWithSigned.setClaimID(c.getClaimID());
        claimWithSigned.setDate(c.getDate());
        claimWithSigned.setUserId(c.getUserId());
        return claimWithSigned;
      })
      .collect(Collectors.toList());
  }

  @Override
  public UUID createClaim(CreateBackofficeClaimDTO claimData, String token) {
    return client.createClaim(claimData, token).getClaimId();
  }

  @Override
  public void markEmployeeClaim(EmployeeClaimRequestDTO dto, String token) {
    client.markEmployeeClaim(dto, token);
  }

  @Override
  public ResponseEntity<ClaimsFilesUploadDTO> allClaimsFiles(UUID claimId) {
    return client.allClaimsFiles(claimId);
  }

  @Override
  public ResponseEntity<Void> uploadClaimsFiles(UUID claimId, MultipartFile claimFile, UploadResult uploadResults) throws IOException {

    ArrayList claimFiles = new ArrayList<ClaimFileDTO>();
    ClaimsFilesUploadDTO claimsFilesUploadDTO = new ClaimsFilesUploadDTO(claimFiles);

    ClaimFileDTO claimFileDTO = new ClaimFileDTO(
      Long.parseLong("1234"),
      uploadResults.getBucket(),
      uploadResults.getKey(),
      claimId,
      claimFile.getContentType(),
      claimFile.getBytes(),
      claimFile.getOriginalFilename(),
      UUID.randomUUID(),
      "",
      claimFile.getSize(),
      "1234"
    );
    claimFiles.add(claimFileDTO);

    return client.uploadClaimsFiles(claimsFilesUploadDTO);
  }

  private String signAudioUrl(String audioUrl) {
    if (audioUrl == null || audioUrl.equals("ManualClaim")) {
      return audioUrl;
    }
    String[] split = audioUrl.split("/");
    String key = split[split.length - 1];
    return amazonS3.generatePresignedUrl(bucketName, key, new Date(Instant.now().plus(30, ChronoUnit.MINUTES).toEpochMilli()), HttpMethod.GET).toString();
  }
}
