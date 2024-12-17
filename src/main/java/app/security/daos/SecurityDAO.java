package app.security.daos;


import app.entities.Badge;
import app.entities.Comment;
import app.exceptions.DaoException;
import app.security.entities.Role;
import app.security.entities.User;
import app.security.exceptions.ApiException;
import app.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
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

    public User createUser(app.dtos.UserDTO dto) throws ValidationException {
        String standardRole = "USER";
        try (EntityManager em = getEntityManager()) {
            User userEntity = em.find(User.class, dto.getUsername());
            if (userEntity != null)
                throw new EntityExistsException("User with username: " + dto.getUsername() + " already exists");

            userEntity = new User(dto);

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
    public app.dtos.UserDTO getLoginDTO(String username) {
        try (EntityManager em = getEntityManager()) {
            User user = em.find(User.class, username);
            if (user == null)
                throw new EntityNotFoundException("No user found with username: " + username); //RuntimeException
            return app.dtos.UserDTO.getLoginDTO(user);
        }
    }

    @Override
    public List<app.dtos.UserDTO> readAllUsers() {
        try (EntityManager em = getEntityManager()) {
            List<User> userList = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            if (userList.isEmpty())
                throw new EntityNotFoundException("No users found"); //RuntimeException
            return app.dtos.UserDTO.fromUserDTOList(userList);
        }
    }

    @Override
    public app.dtos.UserDTO readUser(String username) {
        try (EntityManager em = getEntityManager()) {
            User user = em.find(User.class, username);
            if (user == null)
                throw new EntityNotFoundException("No user found with username: " + username); //RuntimeException
            return app.dtos.UserDTO.getTrimmedDTO(user);
        }
    }

    @Override
    public app.dtos.UserDTO updateUserInfo(String username, app.dtos.UserDTO userDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            User user = em.find(User.class, username);
            if (user == null) {
                throw new DaoException.EntityNotFoundException(User.class, username);
            }
            user.clearBadges();
            List<Integer> commentIdsDTO = userDTO.getComments().stream().map(Comment::getId).toList();
            List<Badge> badgeList = em.createQuery("SELECT b FROM Badge b", Badge.class).getResultList();
            for (Badge badge : badgeList) {
                boolean isMatch = userDTO.getBadges().stream()
                        .anyMatch(dto ->
                                dto.getId().equals(badge.getId()) &&
                                        dto.getName().equals(badge.getName()) &&
                                        dto.getDescription().equals(badge.getDescription())
                        );
                if (isMatch) {
                    user.addBadge(badge);
                    em.merge(user);
                    em.merge(badge);
                }
            }
            List<Comment> commentsToBeRemoved = new ArrayList<>();
                for (Comment userComment : user.getComments()) {
                    if (!commentIdsDTO.contains(userComment.getId())){
                        commentsToBeRemoved.add(userComment);
                    }
                }
            System.out.println(commentsToBeRemoved);
            user.copyInfoFromDto(userDTO);
            User mergedUser = em.merge(user);
            commentsToBeRemoved.forEach(comment -> {comment.setUser(null); em.createQuery("delete from Comment c where c.id = " + comment.getId()).executeUpdate();});
            em.getTransaction().commit();
            return mergedUser != null ? app.dtos.UserDTO.getLoginDTO(mergedUser) : null;
        } catch (Exception e) {
            throw new DaoException.EntityUpdateException(User.class, username, e);
        }
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

