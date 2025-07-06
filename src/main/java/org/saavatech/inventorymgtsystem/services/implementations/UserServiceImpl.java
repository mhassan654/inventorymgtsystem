package org.saavatech.inventorymgtsystem.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.saavatech.inventorymgtsystem.dtos.LoginRequest;
import org.saavatech.inventorymgtsystem.dtos.RegisterRequest;
import org.saavatech.inventorymgtsystem.dtos.Response;
import org.saavatech.inventorymgtsystem.dtos.UserDto;
import org.saavatech.inventorymgtsystem.enums.UserRole;
import org.saavatech.inventorymgtsystem.exceptions.InvalidCredentialsException;
import org.saavatech.inventorymgtsystem.exceptions.NotFoundException;
import org.saavatech.inventorymgtsystem.models.User;
import org.saavatech.inventorymgtsystem.repositories.UserRepository;
import org.saavatech.inventorymgtsystem.security.JwtUtils;
import org.saavatech.inventorymgtsystem.services.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private  final JwtUtils jwtUtils;

    @Override
    public Response registerUser(RegisterRequest registerRequest) {
        UserRole role = UserRole.MANAGER;

        if (registerRequest.getRole() != null) {
            role = registerRequest.getRole();
        }
        User userToSave = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .phoneNumber(registerRequest.getPhoneNumber())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role).build();
        userRepository.save(userToSave);

        return Response.builder()
                .status(200)
                .message("User was successfully registered").build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Invalid email"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }
        String token = jwtUtils.generateToken(user.getEmail());

        return Response.builder()
                .status(200)
                .role(user.getRole())
                .token(token)
                .message("User logged in Successfully").build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        users.forEach(user ->user.setTransactions(null));

        List<UserDto> userDtos = modelMapper.map(users,new TypeToken<List<UserDto>>() {}.getType());

        return Response.builder()
        .status(200)
                .message("success")
                .users(userDtos)
                .build();

    }

    @Override
    public Response getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));

        UserDto userDto = modelMapper.map(user,UserDto.class);
        userDto.setTransactions(null);
        return Response.builder()
                .status(200)
                .message("success")
                .user(userDto)
                .build();
    }

    @Override
    public Response updateUser(Long id, UserDto userDto) {
       User existingUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));

       if (userDto.getEmail() != null) existingUser.setEmail(userDto.getEmail());
       if (userDto.getPhoneNumber() != null) existingUser.setPhoneNumber(userDto.getPhoneNumber());
       if (userDto.getName() != null) existingUser.setName(userDto.getName());
       if (userDto.getRole() != null) existingUser.setRole(userDto.getRole());

       if (userDto.getPassword() != null && !userDto.getPassword().isEmpty())
           existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

       userRepository.save(existingUser);

        return Response.builder()
                .status(200)
                .message("User successfully updated")
                .user(userDto)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Invalid user"));

        user.setTransactions(null);
        return user;
    }

    @Override
    public Response deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));
        userRepository.deleteById(id);
        return Response.builder()
                .status(200)
                .message("User successfully deleted")
                .build();
    }

    @Override
    public Response getUserTransactions(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));
        UserDto userDto = modelMapper.map(user,UserDto.class);
        userDto.getTransactions().forEach(transactionDto -> {
            transactionDto.setUser(null);
            transactionDto.setSupplier(null);
        });
        return Response.builder()
                .status(200)
                .message("Success")
                .user(userDto).build();
    }
}
