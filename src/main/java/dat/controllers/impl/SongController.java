package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.SongDAO;
import dat.dtos.SongDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class SongController implements IController<SongDTO, Integer> {

    private final SongDAO dao;

    public SongController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = SongDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        SongDTO songDTO = dao.read(id);
        ctx.res().setStatus(200);
        ctx.json(songDTO, SongDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        List<SongDTO> songDTOS = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(songDTOS, SongDTO.class);
    }

    @Override
    public void create(Context ctx) {
        SongDTO jsonRequest = ctx.bodyAsClass(SongDTO.class);
        SongDTO songDTO = dao.create(jsonRequest);
        ctx.res().setStatus(201);
        ctx.json(songDTO, SongDTO.class);
    }

    @Override
    public void update(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        SongDTO songDTO = dao.update(id, validateEntity(ctx));
        ctx.res().setStatus(200);
        ctx.json(songDTO, SongDTO.class);
    }

    @Override
    public void delete(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        dao.delete(id);
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return dao.validatePrimaryKey(integer);
    }

    @Override
    public SongDTO validateEntity(Context ctx) {    // TODO add checks once entities and DTOs are implemented
        return ctx.bodyValidator(SongDTO.class)
//                .check()
                .get();
    }
}
