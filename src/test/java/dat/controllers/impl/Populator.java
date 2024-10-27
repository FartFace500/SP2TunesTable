package dat.controllers.impl;

import dat.dtos.ArtistDTO;
import dat.entities.Artist;
import dat.security.entities.Role;
import dat.security.entities.User;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManagerFactory;

public class Populator {

    public static UserDTO[] populateUsers(EntityManagerFactory emf) {
        User user, admin;
        Role userRole, adminRole;

        user = new User("testuser", "user1234");
        admin = new User("testadmin", "admin1234");
        userRole = new Role("USER");
        adminRole = new Role("ADMIN");
        user.addRole(userRole);
        admin.addRole(adminRole);

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.getTransaction().commit();
        }
        UserDTO userDTO = new UserDTO(user.getUsername(), "user1234");
        UserDTO adminDTO = new UserDTO(admin.getUsername(), "admin1234");
        return new UserDTO[]{userDTO, adminDTO};
    }

    public static void populateArtists(EntityManagerFactory emf) {
        ArtistDTO artist1;
        ArtistDTO artist2;

        artist1 = new ArtistDTO();
        artist1.setName("Test Artist 1");
        artist1.setType("artist");

        artist2 = new ArtistDTO();
        artist2.setName("Test Artist 2");
        artist2.setType("artist");

        Artist entity1 = new Artist(artist1);
        Artist entity2 = new Artist(artist2);

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Artist").executeUpdate();
            em.persist(entity1);
            em.persist(entity2);
            em.getTransaction().commit();
        }
    }

}
