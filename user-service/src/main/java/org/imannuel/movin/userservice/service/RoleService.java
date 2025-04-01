package org.imannuel.movin.userservice.service;

import org.imannuel.movin.userservice.enums.Role;

public interface RoleService {
    Role findRole(String role);

    boolean checkRoleAvailability(String name);
}
