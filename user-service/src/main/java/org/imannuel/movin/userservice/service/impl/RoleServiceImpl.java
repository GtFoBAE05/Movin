package org.imannuel.movin.userservice.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.movin.userservice.enums.Role;
import org.imannuel.movin.userservice.repository.RoleRepository;
import org.imannuel.movin.userservice.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @PostConstruct
    @Transactional(rollbackFor = Exception.class)
    void init() {
        log.info("Initializing roles");

        for (Role.ERole value : Role.ERole.values()) {
            if (!roleRepository.existsByName(value)) {
                log.debug("Role {} does not exist, creating...", value.name());
                Role role = Role.builder()
                        .name(value)
                        .build();
                roleRepository.save(role);
                log.info("Created role: {}", value.name());
            } else {
                log.debug("Role {} already exists, skipping...", value.name());
            }
        }
        log.info("Role initialization completed");
    }

    @Override
    @Transactional(readOnly = true)
    public Role findRole(String role) {
        log.debug("Finding role: {}", role);

        Role foundRole = roleRepository.findByName(Role.ERole.valueOf(role))
                .orElseThrow(() -> {
                    log.warn("Role not found: {}", role);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
                });

        log.info("Successfully found role: {}", role);
        return foundRole;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkRoleAvailability(String name) {
        log.debug("Checking if role {} exists", name);
        boolean exists = roleRepository.existsByName(Role.ERole.valueOf(name));
        log.debug("Role {} exists: {}", name, exists);
        return exists;
    }
}