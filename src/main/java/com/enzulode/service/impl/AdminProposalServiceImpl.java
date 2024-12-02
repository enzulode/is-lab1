package com.enzulode.service.impl;

import static com.enzulode.dto.EntityUpdateNotificationDto.NotificationType.*;

import com.enzulode.dao.entity.Proposal;
import com.enzulode.dao.entity.ProposalStatus;
import com.enzulode.dao.repository.ProposalRepository;
import com.enzulode.dto.EntityUpdateNotificationDto;
import com.enzulode.dto.ProposalReadDto;
import com.enzulode.dto.mapper.ProposalMapper;
import com.enzulode.exception.proposal.ProposalNofFoundException;
import com.enzulode.service.AdminProposalService;
import com.enzulode.service.RabbitMQProducerService;
import com.enzulode.util.SecurityContextHelper;
import java.util.Collections;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminProposalServiceImpl implements AdminProposalService {

  private final ProposalMapper proposalMapper;
  private final ProposalRepository proposalRepository;
  private final SecurityContextHelper contextHelper;
  private final Keycloak keycloakClient;
  private final RabbitMQProducerService producerService;

  @Value("${oauth2.realm}")
  private String realm;

  public AdminProposalServiceImpl(
      ProposalMapper proposalMapper,
      ProposalRepository proposalRepository,
      SecurityContextHelper contextHelper,
      Keycloak keycloakClient,
      RabbitMQProducerService producerService) {
    this.proposalMapper = proposalMapper;
    this.proposalRepository = proposalRepository;
    this.contextHelper = contextHelper;
    this.keycloakClient = keycloakClient;
    this.producerService = producerService;
  }

  @Override
  @Transactional
  public void promote() {
    String userId =
        keycloakClient.realm(realm).users().search(contextHelper.findUserName()).getFirst().getId();
    Proposal proposal = new Proposal(userId, ProposalStatus.NONREVIEWED);
    proposalRepository.save(proposal);
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  public Page<ProposalReadDto> findAll(Pageable pageable) {
    return proposalRepository.findAll(pageable).map(proposalMapper::toReadDto);
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public void accept(Long proposalId) {
    Proposal proposal =
        proposalRepository
            .findByIdAndStatus(proposalId, ProposalStatus.NONREVIEWED)
            .orElseThrow(ProposalNofFoundException::new);

    proposal.setStatus(ProposalStatus.ACCEPTED);
    proposalRepository.save(proposal);

    RoleRepresentation adminRole =
        keycloakClient.realm(realm).roles().get("ADMIN").toRepresentation();
    keycloakClient
        .realm(realm)
        .users()
        .get(proposal.getUserId())
        .roles()
        .realmLevel()
        .add(Collections.singletonList(adminRole));

    EntityUpdateNotificationDto updateDto = new EntityUpdateNotificationDto(ADMIN_PROMOTION_ACCEPT);
    producerService.sendToRabbitMQ(updateDto);
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public void decline(Long proposalId) {
    Proposal proposal =
        proposalRepository
            .findByIdAndStatus(proposalId, ProposalStatus.NONREVIEWED)
            .orElseThrow(ProposalNofFoundException::new);

    proposal.setStatus(ProposalStatus.DECLINED);
    proposalRepository.save(proposal);

    EntityUpdateNotificationDto updateDto =
        new EntityUpdateNotificationDto(ADMIN_PROMOTION_DECLINE);
    producerService.sendToRabbitMQ(updateDto);
  }
}
