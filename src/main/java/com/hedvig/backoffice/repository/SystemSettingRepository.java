package com.hedvig.backoffice.repository;

import com.hedvig.backoffice.domain.SystemSetting;
import com.hedvig.backoffice.domain.SystemSettingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {

    @Query("select s from SystemSetting s where s.type = :type")
    Optional<SystemSetting> findByType(@Param("type") SystemSettingType type);

}
