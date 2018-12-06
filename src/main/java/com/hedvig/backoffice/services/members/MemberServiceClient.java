package com.hedvig.backoffice.services.members;

import java.util.List;
import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.members.dto.ChargeMembersDTO;
import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO;
import com.hedvig.backoffice.services.members.dto.MembersSortColumn;
import com.hedvig.backoffice.services.members.dto.MemberDTO;
import com.hedvig.backoffice.web.dto.MemberFraudulentStatusDTO;
import com.hedvig.backoffice.web.dto.MemberStatus;
import com.hedvig.backoffice.services.members.dto.MembersSearchResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-service", url = "${memberservice.baseUrl}",
    configuration = FeignConfig.class)
public interface MemberServiceClient {

  @GetMapping("/i/member/search?status={status}&query={query}")
  List<MemberDTO> search(@PathVariable("status") MemberStatus status,
      @PathVariable("query") String query, @RequestHeader("Authorization") String token);

  @GetMapping("/i/member/searchPaged")
  MembersSearchResultDTO searchPaged(@RequestParam("status") MemberStatus status,
      @RequestParam("query") String query, @RequestParam("page") Integer page,
      @RequestParam("pageSize") Integer pageSize, @RequestParam("sortBy") MembersSortColumn sortBy,
      @RequestParam("sortDirection") Sort.Direction sortDirection,
      @RequestHeader("Authorization") String token);

  @GetMapping("/i/member/{memberId}")
  MemberDTO member(@PathVariable("memberId") String memberId,
      @RequestHeader("Authorization") String token);

  @PostMapping("/i/member/{memberId}/edit")
  void editMember(@PathVariable("memberId") String memberId, @RequestBody MemberDTO dto,
      @RequestHeader("Authorization") String token);

  @PostMapping("/i/member/{memberId}/memberCancelInsurance")
  void cancelInsurance(@PathVariable("memberId") String memberId,
    @RequestBody InsuranceCancellationDTO dto,
    @RequestHeader("Authorization") String token);

  @PostMapping("/i/member/{memberId}/setFraudulentStatus")
  void setFraudulentStatus(
    @PathVariable("memberId") String memberId,
    @RequestBody MemberFraudulentStatusDTO dto,
    @RequestHeader("Authorization") String token);

  @PostMapping("/i/member/many")
  List<MemberDTO> getMembers(@RequestBody ChargeMembersDTO memberIds);
}
