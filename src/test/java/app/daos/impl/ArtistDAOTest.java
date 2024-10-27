package app.daos.impl;

import app.config.HibernateConfig;
import app.dtos.ArtistDTO;
import app.entities.Artist;
import app.exceptions.DaoException;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArtistDAOTest {

    private static EntityManagerFactory emf;
    private static ArtistDAO arDao;

    private ArtistDTO artist1;
    private ArtistDTO artist2;

    @BeforeAll
    static void setUpAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        arDao = ArtistDAO.getInstance(emf);
    }

    @BeforeEach
    void setUp() {
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
            artist1.setArtistId(entity1.getId());
            em.persist(entity2);
            artist2.setArtistId(entity2.getId());
            em.getTransaction().commit();
        }
    }

    @AfterAll
    static void tearDownAll() {
        emf.close();
    }

    @Test
    void read() {
        ArtistDTO foundArtist = arDao.read(Integer.valueOf(artist1.getArtistId()));

        assertNotNull(foundArtist);
        assertEquals(artist1.getName(), foundArtist.getName());
    }

    @Test
    void readAll() {
        List<ArtistDTO> artists = arDao.readAll();
        int expectedSize = 2;   // expected number of artists

        assertEquals(expectedSize, artists.size());
        assertEquals(artist1.getName(), artists.get(0).getName());
        assertEquals(artist2.getName(), artists.get(1).getName());
    }

    @Test
    void create() {
        ArtistDTO newArtist = new ArtistDTO();
        newArtist.setName("New Test Artist");
        newArtist.setType("artist");
        ArtistDTO createdArtist = arDao.create(newArtist);

        assertNotNull(createdArtist.getArtistId());
        assertEquals(newArtist.getName(), createdArtist.getName());
        assertEquals(newArtist.getType(), createdArtist.getType());
    }

    @Test
    void update() {
        ArtistDTO updateArtist = arDao.read(Integer.valueOf(artist1.getArtistId()));

        String newName = "Updated Test Artist 1";
        updateArtist.setName(newName);

        ArtistDTO updatedArtist = arDao.update(Integer.valueOf(artist1.getArtistId()), updateArtist);
        ArtistDTO findArtist = arDao.read(Integer.valueOf(artist1.getArtistId()));

        assertEquals(newName, updatedArtist.getName());
        assertEquals(newName, findArtist.getName());
    }

    @Test
    void delete() {
        arDao.delete(Integer.valueOf(artist1.getArtistId()));

        List<ArtistDTO> artists = arDao.readAll();
        int expectedSize = 1;   // expected number of artists after deletion

        assertEquals(expectedSize, artists.size());
        assertEquals(artist2.getName(), artists.get(0).getName());

        assertThrows(DaoException.EntityNotFoundException.class, () -> {
            arDao.read(Integer.valueOf(artist1.getArtistId()));
        });
    }

}