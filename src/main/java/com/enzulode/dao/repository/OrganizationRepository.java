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

  @Query(value = "select organization_total_rating()", nativeQuery = true)
  double totalRating();

  @Query(value = "select count_organization_full_name_less_than(:c_full_name)", nativeQuery = true)
  int countOrganizationFullNameLessThan(@Param("c_full_name") String fullName);

  @Query(value = "select count_organization_full_name_more_than(:c_full_name)", nativeQuery = true)
  int countOrganizationFullNameMoreThan(@Param("c_full_name") String fullName);

  @Query(value = "select remove_all_employees_on_organization(:org_id)", nativeQuery = true)
  void removeAllEmployeesOnOrganization(@Param("org_id") Integer organizationId);

  @Query(value = "select hire_employee_to_organization(:org_id, :e_id)", nativeQuery = true)
  void hireEmployeeToOrganization(
      @Param("org_id") Integer organizationId, @Param("e_id") Long employeeId);
}
