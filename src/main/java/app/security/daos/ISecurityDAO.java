package app.security.daos;

import app.security.entities.User;
import app.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;

import java.util.List;

public interface ISecurityDAO {
    UserDTO getVerifiedUser(String username, String password) throws ValidationException;
    User createUser(String username, String password);
    User addRole(UserDTO user, String newRole);

    List<app.dtos.UserDTO> readAllUsers();
    app.dtos.UserDTO readUser(String username);
    app.dtos.UserDTO updateUser(String username, app.dtos.UserDTO userDTO);
}
