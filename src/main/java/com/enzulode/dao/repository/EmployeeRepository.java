package com.enzulode.dao.repository;

import com.enzulode.dao.entity.Employee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Optional<Employee> findByIdAndCreatedBy(Long id, String username);

  void deleteByIdAndCreatedBy(Long id, String username);
}
