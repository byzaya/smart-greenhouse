package org.greenhouse.repository.greenhouse;

import org.greenhouse.entity.greenhouse.Greenhouses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GreenhousesRepository extends JpaRepository<Greenhouses, Long> {}
