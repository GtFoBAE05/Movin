package org.imannuel.movin.userservice.repository;

import org.imannuel.movin.userservice.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsByName(Role.ERole name);

    Optional<Role> findByName(Role.ERole name);
}
