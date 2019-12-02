package com.hedvig.backoffice.web.dto;

import com.hedvig.backoffice.services.members.dto.MembersSearchResultDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class MemberPagedSearchResultWebDTO {
  List<MemberWebDTO> members;
  Integer page;
  Integer totalPages;

  public MemberPagedSearchResultWebDTO(MembersSearchResultDTO searchRes) {
    this.page = searchRes.getPage();
    this.totalPages = searchRes.getTotalPages();
    this.members = searchRes.getMembers().stream()
      .map(MemberWebDTO::new)
      .collect(Collectors.toList());
  }
}
