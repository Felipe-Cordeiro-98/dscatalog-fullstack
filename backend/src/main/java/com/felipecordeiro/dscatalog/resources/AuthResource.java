package com.felipecordeiro.dscatalog.resources;

import com.felipecordeiro.dscatalog.dto.AuthRequestDTO;
import com.felipecordeiro.dscatalog.dto.AuthResponseDTO;
import com.felipecordeiro.dscatalog.dto.UserResponseDTO;
import com.felipecordeiro.dscatalog.entities.User;
import com.felipecordeiro.dscatalog.security.JwtService;
import com.felipecordeiro.dscatalog.security.UserDetailsServiceImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthResource {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImp userDetailsService;

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponseDTO(token, new UserResponseDTO((User) userDetails)));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}