package org.greenhouse.repository.log.readings;

import org.greenhouse.entity.log.readings.Light;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LightRepository extends JpaRepository<Light, Long> {}
