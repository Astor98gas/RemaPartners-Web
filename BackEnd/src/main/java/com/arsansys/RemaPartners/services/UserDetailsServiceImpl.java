package com.arsansys.RemaPartners.services;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.arsansys.RemaPartners.models.entities.UserEntity;
import com.arsansys.RemaPartners.repositories.UserRepository;

/**
 * Servei que implementa mètodes pels usuaris.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        /**
         * Mètode per carregar la sessió dels usuaris.
         *
         * @param username Nom d'usuari.
         * @return UserDetails.
         * @throws UsernameNotFoundException Excepció si l'usuari no existeix.
         */
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UserEntity userEntity = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "El usuario: " + username + " no existe."));

                Collection<? extends GrantedAuthority> authorities = Stream.of(userEntity.getRol())
                                .map(rol -> new SimpleGrantedAuthority("ROLE_".concat(rol.getName().name())))
                                .collect(Collectors.toSet());

                return new User(userEntity.getUsername(), userEntity.getPassword(), userEntity.getActive(), true, true,
                                true,
                                authorities);
        }

}
