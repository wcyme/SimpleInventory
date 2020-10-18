package com.johnny.wong.inventory.repository;

import com.johnny.wong.inventory.domain.InternalTransferLog;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the InternalTransferLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InternalTransferLogRepository extends JpaRepository<InternalTransferLog, Long> {

    @Query("select internalTransferLog from InternalTransferLog internalTransferLog where internalTransferLog.user.login = ?#{principal.username}")
    List<InternalTransferLog> findByUserIsCurrentUser();
}
