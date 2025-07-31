package com.felipecordeiro.dscatalog.services;

import com.felipecordeiro.dscatalog.dto.RoleDTO;
import com.felipecordeiro.dscatalog.dto.UserInsertDTO;
import com.felipecordeiro.dscatalog.dto.UserResponseDTO;
import com.felipecordeiro.dscatalog.dto.UserUpdateDTO;
import com.felipecordeiro.dscatalog.entities.Role;
import com.felipecordeiro.dscatalog.entities.User;
import com.felipecordeiro.dscatalog.repositories.RoleRepository;
import com.felipecordeiro.dscatalog.repositories.UserRepository;
import com.felipecordeiro.dscatalog.services.exceptions.DatabaseException;
import com.felipecordeiro.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO insert(UserInsertDTO userInsertDTO) {
        User user = new User();
        copyDtoToEntity(userInsertDTO, user);
        user.setPassword(passwordEncoder.encode(userInsertDTO.password()));
        user = userRepository.save(user);
        return new UserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO userUpdateDTO) {
        try {
            User user = userRepository.getReferenceById(id);
            copyDtoToEntity(userUpdateDTO, user);
            user = userRepository.save(user);
            return new UserResponseDTO(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation: cannot delete user");
        }
    }

    private void copyDtoToEntity(UserInsertDTO userInsertDTO, User user) {
        user.setFirstName(userInsertDTO.firstName());
        user.setLastName(userInsertDTO.lastName());
        user.setEmail(userInsertDTO.email());

        user.getRoles().clear();
        for (RoleDTO roleDTO : userInsertDTO.roles()) {
            Role role = roleRepository.getReferenceById(roleDTO.id());
            user.getRoles().add(role);
        }
    }

    private void copyDtoToEntity(UserUpdateDTO dto, User user) {
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());

        user.getRoles().clear();
        for (RoleDTO roleDTO : dto.roles()) {
            Role role = roleRepository.getReferenceById(roleDTO.id());
            user.getRoles().add(role);
        }
    }

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