package dat.routes;

import dat.controllers.impl.ArtistController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ArtistRoute {

    private final ArtistController artistController = new ArtistController();

    protected EndpointGroup getRoutes() {

        return () -> {
            post("/", artistController::create, Role.ADMIN);
            post("/add_album", artistController::addAlbum, Role.ADMIN); //TODO: make id/artistId and id2/albumId part of the request
            get("/", artistController::readAll, Role.ANYONE, Role.USER);
            get("/{id}", artistController::read, Role.ANYONE, Role.USER);
            put("/{id}", artistController::update, Role.ADMIN);
            delete("/{id}", artistController::delete, Role.ADMIN);
        };
    }
}