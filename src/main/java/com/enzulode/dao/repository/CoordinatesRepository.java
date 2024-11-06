package com.enzulode.dao.repository;

import com.enzulode.dao.entity.Coordinates;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {

  Optional<Coordinates> findByIdAndCreatedBy(Long id, String username);

  void deleteByIdAndCreatedBy(Long id, String username);
}
