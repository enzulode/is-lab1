package com.enzulode.dao.repository;

import com.enzulode.dao.entity.Organization;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

  Optional<Organization> findByIdAndCreatedBy(Integer id, String username);

  Page<Organization> findByCreatedBy(String username, Pageable pageable);

  void deleteByIdAndCreatedBy(Integer id, String username);

  @Query(value = "select organization_total_rating()")
  double totalRating();

  @Query("select count_organization_full_name_less_than(:c_full_name)")
  int countOrganizationFullNameLessThan(@Param("c_full_name") String fullName);

  @Query("select count_organization_full_name_more_than(:c_full_name)")
  int countOrganizationFullNameMoreThan(@Param("c_full_name") String fullName);

  @Query("select remove_all_employees_on_organization(:org_id)")
  void removeAllEmployeesOnOrganization(@Param("org_id") Integer organizationId);

  @Query("select hire_employee_to_organization(:org_id, :e_id)")
  void hireEmployeeToOrganization(
      @Param("org_id") Integer organizationId, @Param("e_id") Long employeeId);
}
