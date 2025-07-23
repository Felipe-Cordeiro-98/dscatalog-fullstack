package com.felipecordeiro.dscatalog.services;

import com.felipecordeiro.dscatalog.dto.RoleDTO;
import com.felipecordeiro.dscatalog.dto.UserRequestDTO;
import com.felipecordeiro.dscatalog.dto.UserResponseDTO;
import com.felipecordeiro.dscatalog.entities.Role;
import com.felipecordeiro.dscatalog.entities.User;
import com.felipecordeiro.dscatalog.repositories.RoleRepository;
import com.felipecordeiro.dscatalog.repositories.UserRepository;
import com.felipecordeiro.dscatalog.services.exceptions.DatabaseException;
import com.felipecordeiro.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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
    public UserResponseDTO insert(UserRequestDTO userRequestDTO) {
        User user = new User();
        copyDtoToEntity(userRequestDTO, user);
        user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        user = userRepository.save(user);
        return new UserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO userRequestDTO) {
        try {
            User user = userRepository.getReferenceById(id);
            copyDtoToEntity(userRequestDTO, user);
            user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
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

    private void copyDtoToEntity(UserRequestDTO userRequestDTO, User user) {
        user.setFirstName(userRequestDTO.firstName());
        user.setLastName(userRequestDTO.lastName());
        user.setEmail(userRequestDTO.email());

        user.getRoles().clear();
        for (RoleDTO roleDTO : userRequestDTO.roles()) {
            Role role = roleRepository.getReferenceById(roleDTO.id());
            user.getRoles().add(role);
        }
    }
}