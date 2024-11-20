package com.enzulode.dao.repository;

import com.enzulode.dao.entity.Proposal;
import com.enzulode.dao.entity.ProposalStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {

  Optional<Proposal> findByIdAndStatus(Long id, ProposalStatus status);
}
