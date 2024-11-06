package com.enzulode.dao.repository;

import com.enzulode.dao.entity.Location;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

  Optional<Location> findByIdAndCreatedBy(Long id, String username);

  void deleteByIdAndCreatedBy(Long id, String username);
}
