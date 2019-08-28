package com.hedvig.backoffice.services.members.dto;

  import lombok.Value;

@Value
public class WhitelistMemberDTO {
  String memberId;
  String approverEmail;
}

