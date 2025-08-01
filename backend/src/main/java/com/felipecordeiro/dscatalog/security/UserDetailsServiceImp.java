package com.felipecordeiro.dscatalog.security;

import com.felipecordeiro.dscatalog.entities.User;
import com.felipecordeiro.dscatalog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImp.class);
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            logger.error("User not found: " + username);
            throw new UsernameNotFoundException("Email not found: " + username);
        }
        logger.info("User found: " + username);
        return user;
    }
}
