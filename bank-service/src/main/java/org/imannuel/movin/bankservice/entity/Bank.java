package org.imannuel.movin.bankservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "m_banks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "fee", nullable = false)
    private Integer fee;

    @Column(name = "image", nullable = false)
    private String image;

    private boolean status;
}
