package com.arsansys.RemaPartners.models.entities;

import java.sql.Date;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un usuari.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    /**
     * Identificador únic de l'usuari.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    @Size(max = 250)
    private String password;

    // /**
    // * Indica si l'usuari està actiu.
    // */
    // @Builder.Default
    // private Boolean active = true;

    // /**
    // * Conjunt de rols que té assignats l'usuari.
    // */
    // @ManyToMany(fetch = FetchType.EAGER, targetEntity = RolEntity.class, cascade
    // = CascadeType.PERSIST)
    // @JoinTable(name = "users_rols", joinColumns = @JoinColumn(name =
    // "usuari_id"), inverseJoinColumns = @JoinColumn(name = "rols_id"))
    // private Set<RolEntity> rols;
}
