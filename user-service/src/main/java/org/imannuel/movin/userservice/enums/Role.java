package org.imannuel.movin.userservice.enums;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ERole name;

    public enum ERole {
        ROLE_CUSTOMER,
        ROLE_ADMIN,
    }
}
