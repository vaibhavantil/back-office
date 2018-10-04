package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO;
import com.hedvig.backoffice.web.dto.MemberDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
    name = "member-service",
    url = "${memberservice.baseUrl}",
    configuration = FeignConfig.class,
    fallback = MemberServiceClientFallback.class)
public interface MemberServiceClient {

  @GetMapping("/i/member/search?status={status}&query={query}")
  List<MemberDTO> search(
      @PathVariable("status") String status,
      @PathVariable("query") String query,
      @RequestHeader("Authorization") String token);

  @GetMapping("/i/member/{memberId}")
  MemberDTO member(
      @PathVariable("memberId") String memberId, @RequestHeader("Authorization") String token);

  @PostMapping("/i/member/{memberId}/edit")
  void editMember(
      @PathVariable("memberId") String memberId,
      @RequestBody MemberDTO dto,
      @RequestHeader("Authorization") String token);

  @PostMapping("/i/member/{memberId}/memberCancelInsurance")
  void cancelInsurance(
      @PathVariable("memberId") String memberId,
      @RequestBody InsuranceCancellationDTO dto,
      @RequestHeader("Authorization") String token);

  @GetMapping("/i/member/many/{memberIds}")
  List<MemberDTO> getMembers(@PathVariable("memberIds") List<String> memberIds);
}
