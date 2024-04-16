package org.greenhouse.repository.log;

import org.greenhouse.entity.log.Topics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicsRepository extends JpaRepository<Topics, Integer> {}
