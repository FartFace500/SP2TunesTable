package dat.routes;

import dat.controllers.impl.AlbumController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class AlbumRoute {

    private final AlbumController albumController = new AlbumController();

    protected EndpointGroup getRoutes() {

        return () -> {
            post("/", albumController::create, Role.ADMIN);
            get("/", albumController::readAll, Role.ANYONE, Role.USER);
            get("/{id}", albumController::read, Role.ANYONE, Role.USER);
            put("/{id}", albumController::update, Role.ADMIN);
            delete("/{id}", albumController::delete, Role.ADMIN); //make this turn all songs into singles or add a word to indicate they are former album songs
        };
    }
}