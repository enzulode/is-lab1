package com.enzulode.dao.repository;

import com.enzulode.dao.entity.Address;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

  Optional<Address> findByIdAndCreatedBy(Long id, String username);

  Page<Address> findByCreatedBy(String username, Pageable pageable);

  void deleteByIdAndCreatedBy(Long id, String username);
}
