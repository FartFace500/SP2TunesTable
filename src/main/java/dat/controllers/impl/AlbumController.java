package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.AlbumDAO;
import dat.dtos.AlbumDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class AlbumController implements IController<AlbumDTO, Integer> {

    private final AlbumDAO dao;

    public AlbumController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = AlbumDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        // request
        Integer id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // DTO
        AlbumDTO albumDTO = dao.read(id);
        // response
        ctx.res().setStatus(200);
        ctx.json(albumDTO, AlbumDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        // List of DTOS
         List<AlbumDTO> albumDTOS = dao.readAll();
        // response
         ctx.res().setStatus(200);
         ctx.json(albumDTOS, AlbumDTO.class);
    }

    @Override
    public void create(Context ctx) {
        // request
        AlbumDTO jsonRequest = ctx.bodyAsClass(AlbumDTO.class);
        // DTO
        AlbumDTO albumDTO = dao.create(jsonRequest);
        // response
        ctx.res().setStatus(201);
        ctx.json(albumDTO, AlbumDTO.class);
    }

    @Override
    public void update(Context ctx) {
        // request
        Integer id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // dto
        AlbumDTO albumDTO = dao.update(id, validateEntity(ctx));
        // response
        ctx.res().setStatus(200);
        ctx.json(albumDTO, AlbumDTO.class);
    }

    @Override
    public void delete(Context ctx) {
        // request
        Integer id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        dao.delete(id);
        // response
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return dao.validatePrimaryKey(integer);
    }

    @Override

    public AlbumDTO validateEntity(Context ctx) {       // TODO add checks
        return ctx.bodyValidator(AlbumDTO.class)
//                .check(dto -> dto.getName() != null && dto.getName().length() > 0, "Name cannot be null or empty")
                .get();
    }
}
