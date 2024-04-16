package org.greenhouse.repository.log;

import org.greenhouse.entity.log.SendLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendLogsRepository extends JpaRepository<SendLogs, Integer> {}
