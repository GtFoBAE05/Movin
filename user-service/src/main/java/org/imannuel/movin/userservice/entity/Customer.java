package org.imannuel.movin.userservice.entity;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "m_customers")
@DiscriminatorValue("CUSTOMER")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Customer extends User {
    @Column(name = "referral_code", nullable = false)
    private String referralCode;

    @Column(name = "is_phone_verified", nullable = false)
    private boolean isPhoneVerified = false;

    @Column(name = "is_email_verified", nullable = false)
    private boolean isEmailVerified = false;

    @Column(name = "is_identity_verified", nullable = false)
    private boolean isIdentityVerified = false;

    @Column(name = "balance", nullable = false)
    private Integer balance = 0;
}
