package tsp.utils;

import tsp.services.auth.AuthService;

import javax.ejb.EJB;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Provider
public class RequestAuthFilter implements ContainerRequestFilter {

    @EJB
    private AuthService authService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (!requestContext.getUriInfo().getPath().equals("auth") &&
                !requestContext.getUriInfo().getPath().equals("user/save") &&
                !requestContext.getUriInfo().getPath().startsWith("echo")) {
            List<String> token = requestContext.getHeaders().get("authorization");
            if (token == null || authService.getNameByToken(token.get(0)) == null) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }
    }
}
