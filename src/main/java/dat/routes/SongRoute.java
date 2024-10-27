package dat.routes;

import dat.controllers.impl.SongController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SongRoute {

    private final SongController songController = new SongController();

    protected EndpointGroup getRoutes() {

        return () -> {
            post("/", songController::create, Role.ADMIN);
            get("/", songController::readAll, Role.ANYONE, Role.USER);
            get("/{id}", songController::read, Role.ANYONE, Role.USER);
            put("/{id}", songController::update, Role.ADMIN);
            delete("/{id}", songController::delete, Role.ADMIN);
        };
    }
}