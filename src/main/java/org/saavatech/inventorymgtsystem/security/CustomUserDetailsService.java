package org.saavatech.inventorymgtsystem.security;

import lombok.RequiredArgsConstructor;
import org.saavatech.inventorymgtsystem.exceptions.NotFoundException;
import org.saavatech.inventorymgtsystem.models.User;
import org.saavatech.inventorymgtsystem.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByEmail(username)
               .orElseThrow(()-> new NotFoundException("User email not found"));
       return AuthUser.builder().user(user).build();
    }
}
