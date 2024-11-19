package com.enzulode.dao.repository;

import com.enzulode.dao.entity.Organization;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

  Optional<Organization> findByIdAndCreatedBy(Integer id, String username);

  void deleteByIdAndCreatedBy(Integer id, String username);

  @Query(value = "select organization_total_rating()")
  double totalRating();

  @Query("select count_organization_full_name_less_than(:c_full_name)")
  int countOrganizationFullNameLessThan(@Param("c_full_name") String fullName);
}
