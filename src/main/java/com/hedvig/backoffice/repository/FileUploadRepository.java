package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.FileUpload;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface FileUploadRepository extends CrudRepository<FileUpload, Long> {

  //@Query("select * from FileUpload f where f.memberId = :memberId")
  //ArrayList<FileUpload> findByMemberId(@Param("memberId") String memberId);

}
