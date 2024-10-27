package dat.routes;

import dat.Populate;
import dat.security.enums.Role;
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
