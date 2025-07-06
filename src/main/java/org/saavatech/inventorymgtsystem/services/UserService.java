package org.saavatech.inventorymgtsystem.services;

import org.saavatech.inventorymgtsystem.dtos.LoginRequest;
import org.saavatech.inventorymgtsystem.dtos.RegisterRequest;
import org.saavatech.inventorymgtsystem.dtos.Response;
import org.saavatech.inventorymgtsystem.dtos.UserDto;
import org.saavatech.inventorymgtsystem.models.User;

public interface UserService {
    Response registerUser(RegisterRequest registerRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();
    Response getUserById(Long id);
    Response updateUser(Long id, UserDto userDto);
    User getCurrentLoggedInUser();
    Response deleteUser(Long id);
    Response getUserTransactions(Long id);
}
