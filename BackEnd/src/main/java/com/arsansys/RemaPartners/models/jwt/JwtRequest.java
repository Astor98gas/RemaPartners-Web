package com.arsansys.RemaPartners.models.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtRequest {

    String username;
    String password;
    String googleToken;

}
