package tsp.utils;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
public class ResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestCtx, ContainerResponseContext responseCtx ) throws IOException {
//        final String allowHeaders = requestCtx.getHeaderString("access-control-request-headers");
//        final String allowMethods = requestCtx.getHeaderString("access-control-request-method");
//        final String allowOrigin = requestCtx.getHeaderString("origin");

//        responseCtx.getHeaders().add( "Access-Control-Allow-Origin", allowOrigin);
        responseCtx.getHeaders().add( "Access-Control-Allow-Origin", "*");
        responseCtx.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
        responseCtx.getHeaders().add("Access-Control-Allow-Headers", "origin, Content-Type, content-type, accept, authorization, Authorization");
//        responseCtx.getHeaders().add("Access-Control-Allow-Headers", allowHeaders);
//        responseCtx.getHeaders().add( "Access-Control-Allow-Methods", allowMethods);
        responseCtx.getHeaders().add("Access-Control-Expose-Headers", "authorization, Content-Type, content-type");
        responseCtx.getHeaders().add( "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
    }
}
