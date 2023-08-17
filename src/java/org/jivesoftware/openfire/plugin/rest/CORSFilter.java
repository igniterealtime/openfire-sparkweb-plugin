package org.jivesoftware.openfire.plugin.rest;

import javax.annotation.Priority;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class CORSFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger Log = LogManager.getLogger(CORSFilter.class);	
    private static final String HEADER_ORIGIN = "Origin";
    private static final String HEADER_ACCESS_CONTROL_ALLOW_PNETWORK = "Access-Control-Allow-Private-Network";
    private static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private static final String HEADER_ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    private static final String HEADER_ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String HEADER_ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    private static final String HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    
	private Set<String> allowedOrigins = new HashSet<>();

    public Set<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String origin = requestContext.getHeaderString(HEADER_ORIGIN);
		Log.debug("filer 1 " + origin + " " + requestContext.getMethod());
		
        if (origin == null) {
            return;
        }
        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
            preFlight(origin, requestContext);
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String origin = requestContext.getHeaderString(HEADER_ORIGIN);
		Log.debug("filer 2 " + origin + " " + requestContext.getMethod());
		
        if (origin == null) {
            return;
        }

        responseContext.getHeaders().putSingle(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        responseContext.getHeaders().putSingle(HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
		responseContext.setStatus(Response.Status.OK.getStatusCode());			
    }


    private void preFlight(String origin, ContainerRequestContext requestContext) throws IOException {
        Response.ResponseBuilder builder = Response.ok();
        builder.header(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        builder.header(HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        builder.header(HEADER_ACCESS_CONTROL_ALLOW_PNETWORK, "true");

        String requestMethods = requestContext.getHeaderString(HEADER_ACCESS_CONTROL_REQUEST_METHOD);
        if (requestMethods != null) {
            builder.header(HEADER_ACCESS_CONTROL_ALLOW_METHODS, requestMethods);
        }

        String allowHeaders = requestContext.getHeaderString(HEADER_ACCESS_CONTROL_REQUEST_HEADERS);
        if (allowHeaders != null) {
            builder.header(HEADER_ACCESS_CONTROL_ALLOW_HEADERS, allowHeaders);
        }
		
		Log.debug("preFlight " + origin + " " + builder);
        requestContext.abortWith(builder.build());
    }
}
