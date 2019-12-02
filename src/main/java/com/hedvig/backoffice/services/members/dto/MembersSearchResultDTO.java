package com.hedvig.backoffice.services.members.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MembersSearchResultDTO {
  List<MemberDTO> members;
  Integer page;
  Integer totalPages;
}
