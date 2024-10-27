package app.controllers.impl;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.dtos.ArtistDTO;
import app.entities.Artist;
import app.security.controllers.SecurityController;
import app.security.daos.SecurityDAO;
import app.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArtistControllerTest {

    private final static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private final static SecurityController securityController = SecurityController.getInstance();
    private final static SecurityDAO securityDAO = new SecurityDAO(emf);
    private static Javalin app;
    private static UserDTO userDTO, adminDTO;
    private static String userToken, adminToken;
    private static final int TEST_PORT = 7000;

    @BeforeAll
    static void setUpAll() {
        HibernateConfig.setTest(true);

        app = ApplicationConfig.startServer(TEST_PORT);

        RestAssured.baseURI = "http://localhost:" + TEST_PORT + "/api";
    }

    @AfterAll
    static void tearDownAll() {
        ApplicationConfig.stopServer(app);
        emf.close();
    }

    @BeforeEach
    void setUp() {
        Populator.populateArtists(emf);

        UserDTO[] users = Populator.populateUsers(emf);
        userDTO = users[0];
        adminDTO = users[1];
        try {
            UserDTO verifiedUser = securityDAO.getVerifiedUser(userDTO.getUsername(), userDTO.getPassword());
            UserDTO verifiedAdmin = securityDAO.getVerifiedUser(adminDTO.getUsername(), adminDTO.getPassword());
            userToken = "Bearer " + securityController.createToken(verifiedUser);
            adminToken = "Bearer " + securityController.createToken(verifiedAdmin);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Album ").executeUpdate();
            em.createQuery("DELETE FROM Artist ").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE album_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE artist_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }
    }

    // error from post possibly not working. This code should work once security is implemented.
    // This would also make the entire test class more uniform.
//    @BeforeEach
//    void setUp() {
//        clearArtists();
//        createArtist("Test Artist 1");
//        createArtist("Test Artist 2");
//    }
//
//    // Helper methods
//    private void clearArtists() {
//        given()
//                .when()
//                .delete("/artists")
//                .then()
//                .statusCode(204);
//    }
//
//    private void createArtist(String name) {
//        given()
//                .contentType("application/json")
//                .body("{\"name\":\"" + name + "\", \"type\":\"artist\"}")
//                .when()
//                .post("/artists")
//                .then()
//                .statusCode(201);
//    }

    @Test
    void read() {
        System.out.println("userToken: " + userToken);
        System.out.println("adminToken: " + adminToken);
        int artistId = 1;
        ArtistDTO artist =
                given()
                    .when()
                    .header("Authorization", userToken)
                    .get("/artists/{id}", artistId)
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("Test Artist 1"))
                    .extract()
                    .as(new TypeRef<ArtistDTO>() {});

        assertThat(artist.getName(), is("Test Artist 1"));
        assertThat(artist.getType(), is("artist"));
    }

    @Test
    void readAll() {
        System.out.println("userToken: " + userToken);
        System.out.println("adminToken: " + adminToken);
        List<ArtistDTO> artists =
                given()
                    .when()
                    .header("Authorization", userToken)
                    .get("/artists")
                    .then()
                    .statusCode(200)
                    .body("size()", equalTo(2)) // Expecting two artists created in setup
                    .extract()
                    .as(new TypeRef<List<ArtistDTO>>() {});

        assertThat(artists.size(), is(2));
        assertThat(artists.get(0).getName(), is("Test Artist 1"));
        assertThat(artists.get(1).getName(), is("Test Artist 2"));
    }

    @Test
    void create() {
        System.out.println("userToken: " + userToken);
        System.out.println("adminToken: " + adminToken);
        ArtistDTO newArtist =
                given()
                    .contentType("application/json")
                    .header("Authorization", adminToken)
                    .body("{\"name\":\"Test Artist 3\", \"type\":\"artist\"}")
                    .when()
                    .post("/artists")
                    .then()
                    .statusCode(201)
                    .extract()
                    .as(new TypeRef<ArtistDTO>() {});

        assertThat(newArtist.getArtistId(), is(notNullValue()));
        assertThat(newArtist.getName(), is("Test Artist 3"));

        List<ArtistDTO> artists =
                given()
                        .header("Authorization", userToken)
                        .when()
                        .get("/artists")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(new TypeRef<List<ArtistDTO>>() {});

        assertThat(artists.size(), is(3));
    }

    @Test
    void update() {
        System.out.println("userToken: " + userToken);
        System.out.println("adminToken: " + adminToken);
        int artistId = 1;
        ArtistDTO updatedArtist =
                given()
                        .contentType("application/json")
                        .header("Authorization", adminToken)
                        .body("{\"name\":\"Updated Artist 1\", \"type\":\"artist\"}")
                        .when()
                        .put("/artists/{id}", artistId)
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(new TypeRef<ArtistDTO>() {});

        assertThat(updatedArtist.getArtistId(), is(artistId));
        assertThat(updatedArtist.getName(), is("Updated Artist 1"));
    }

    @Test
    void delete() {
        System.out.println("userToken: " + userToken);
        System.out.println("adminToken: " + adminToken);
        int artistId = 1;
        int artistsAlreadyInTestDB = 2;

        given()
                .contentType("application/json")
                .header("Authorization", adminToken)
                .when()
                .delete("/artists/{id}", artistId)
                .then()
                .statusCode(204);

        // Check that the artist was deleted
        given()
                .contentType("application/json")
                .when()
                .get("/artists/{id}", artistId)
                .then()
                .statusCode(400);       // TODO should ideally be 404, add error handling

        List<ArtistDTO> allArtistsAfterDeletion =
                given()
                        .header("Authorization", userToken)
                        .when()
                        .get("/artists")
                        .then()
                        .statusCode(200)
                        .extract()
                        .as(new TypeRef<List<ArtistDTO>>() {});

        assertThat(allArtistsAfterDeletion.size(), is(artistsAlreadyInTestDB - 1));
    }

}