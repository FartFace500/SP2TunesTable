package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.ArtistDAO;
import dat.dtos.ArtistDTO;
import dat.dtos.special.RequestDTO;
import dat.entities.Album;
import dat.entities.Artist;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ArtistController implements IController<ArtistDTO, Integer> {

    private final ArtistDAO dao;

    public ArtistController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = ArtistDAO.getInstance(emf);
    }

    public void addAlbum(Context ctx) {
        RequestDTO jsonRequest = ctx.bodyAsClass(RequestDTO.class);
        System.out.println(jsonRequest.toString());
        ArtistDTO artistDTO = dao.read(jsonRequest.getArtistId());
        Artist artist = new Artist(artistDTO);
        try (var em = HibernateConfig.getEntityManagerFactory().createEntityManager()) {
            em.getTransaction().begin();
            Album album = em.find(Album.class, jsonRequest.getAlbumId());
            artist.addAlbum(album);
            em.merge(artist);
            em.getTransaction().commit();
        }
        ctx.res().setStatus(200);
        ctx.json(new ArtistDTO(artist), ArtistDTO.class);
    }

    @Override
    public void read(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        ArtistDTO artistDTO = dao.read(id);
        ctx.res().setStatus(200);
        ctx.json(artistDTO, ArtistDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        List<ArtistDTO> artistDTOS = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(artistDTOS, ArtistDTO.class);
    }

    @Override
    public void create(Context ctx) {
        ArtistDTO jsonRequest = ctx.bodyAsClass(ArtistDTO.class);
        ArtistDTO artistDTO = dao.create(jsonRequest);
        ctx.res().setStatus(201);
        ctx.json(artistDTO, ArtistDTO.class);
    }

    @Override
    public void update(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        ArtistDTO artistDTO = dao.update(id, validateEntity(ctx));
        ctx.res().setStatus(200);
        ctx.json(artistDTO, ArtistDTO.class);
    }

    @Override
    public void delete(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        dao.delete(id);
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return dao.validatePrimaryKey(integer);
    }

    @Override
    public ArtistDTO validateEntity(Context ctx) {      // TODO add needed checks
        return ctx.bodyValidator(ArtistDTO.class)
//                .check(a -> a.getName() != null && !a.getName().isEmpty(), "Invalid name")
                .get();
    }
}
