package app.routes;

import app.Populate;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PopulateRoute {

    Populate populate = new Populate();

    protected EndpointGroup getRoutes() {

        return () -> {
            get("/", populate::runIfEmpty, Role.ANYONE);
        };
    }
}
