package com.enzulode.dao.repository;

import com.enzulode.dao.entity.Organization;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

  Optional<Organization> findByIdAndCreatedBy(Integer id, String username);

  void deleteByIdAndCreatedBy(Integer id, String username);
}
