package dat.security.daos;


import dat.security.entities.Role;
import dat.security.entities.User;
import dat.security.exceptions.ApiException;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;

import java.util.stream.Collectors;


/**
 * Purpose: To handle security in the API
 * Author: Thomas Hartmann
 */
public class SecurityDAO implements ISecurityDAO {

    private static ISecurityDAO instance;
    private static EntityManagerFactory emf;

    public SecurityDAO(EntityManagerFactory _emf) {
        emf = _emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public UserDTO getVerifiedUser(String username, String password) throws ValidationException {
        try (EntityManager em = getEntityManager()) {
            User user = em.find(User.class, username);
            if (user == null)
                throw new EntityNotFoundException("No user found with username: " + username); //RuntimeException
            user.getRoles().size(); // force roles to be fetched from db
            if (!user.verifyPassword(password))
                throw new ValidationException("Wrong password");
            return new UserDTO(user.getUsername(), user.getRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toSet()));
        }
    }

    @Override
    public User createUser(String username, String password) {
        String standardRole = "USER";
        try (EntityManager em = getEntityManager()) {
            User userEntity = em.find(User.class, username);
            if (userEntity != null)
                throw new EntityExistsException("User with username: " + username + " already exists");

            userEntity = new User(username, password);

            em.getTransaction().begin();

            createRoleIfNotPresent(standardRole.toUpperCase());
            Role userRole = em.find(Role.class, standardRole.toUpperCase());

            userEntity.addRole(userRole);

            em.persist(userEntity);
            em.getTransaction().commit();

            return userEntity;
        }catch (Exception e){
            e.printStackTrace();
            throw new ApiException(400, e.getMessage());
        }
    }

    @Override
    public User addRole(UserDTO userDTO, String newRole) {
        try (EntityManager em = getEntityManager()) {
            User user = em.find(User.class, userDTO.getUsername());
            if (user == null)
                throw new EntityNotFoundException("No user found with username: " + userDTO.getUsername());

            em.getTransaction().begin();

            createRoleIfNotPresent(newRole.toUpperCase());

            Role role = em.find(Role.class, newRole.toUpperCase());
            if (role == null) {
                throw new EntityNotFoundException("Role " + newRole + " not found when checked");
            }

            user.addRole(role);
            em.getTransaction().commit();

            return user;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(400, e.getMessage());
        }
    }

    public boolean roleExistCheck(String roleName) {
        try (EntityManager em = getEntityManager()) {
            Role role = em.find(Role.class, roleName.toUpperCase());
            return role != null;
        }
    }

    public void createRoleIfNotPresent(String roleName) {
        if (!roleExistCheck(roleName)) {
            try (EntityManager em = getEntityManager()) {
                em.getTransaction().begin();
                Role role = new Role(roleName.toUpperCase());
                em.persist(role);
                em.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("Failed to create role: " + e.getMessage());
            }
        } else {
            System.out.println("Role " + roleName + " already exists");
        }
    }

}

