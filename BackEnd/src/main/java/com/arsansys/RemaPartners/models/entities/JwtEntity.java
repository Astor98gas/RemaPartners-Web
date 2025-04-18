package com.arsansys.RemaPartners.models.entities;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Document(collection = "jwt")
public class JwtEntity {

    @Id
    private String token;

    @NonNull
    private String username;

    @NonNull
    private Date expirationDate;

    @NonNull
    private Boolean isValid;

}
